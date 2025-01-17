import torch
from torch.utils.data import DataLoader
from transformers import GPT2Tokenizer, GPT2ForSequenceClassification, Trainer, TrainingArguments
from datasets import load_dataset, DatasetDict, concatenate_datasets

# Check if CUDA is available and set the device
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Load the readerbench dataset
dataset = load_dataset("readerbench/ro_fake_news", "eda")

considered_tag_words = ['5g', 'vaccinare', 'controlul']


# Use only "headline" and "body" for the text and "tags" for similarity labels
def preprocess_data(example):
    # Combine the headline and body to form the input text
    example['text'] = example['headline'] + " " + example['body']

    # Check the 'tags' field
    tags = example['tags']
    # print(f"Tags Field: {tags}")  # Debugging the tags field

    # Check if any of the considered tag words appear in example['tags']
    found = any(word in tags.lower() for word in considered_tag_words) if tags else False
    example['label'] = 1.0 if found else 0.0

    # Debugging print statements
    # print(f"Processed Example -> Tags: {tags} | Found: {found} | Label: {example['label']}")

    return example


# Apply preprocessing to each subset in the dataset
dataset = dataset.map(preprocess_data)

# Concatenate all subsets (train, validation, test) into a single dataset
combined_dataset = concatenate_datasets([dataset["train"], dataset["validation"], dataset["test"]])

# Split the combined dataset into a new train and validation set
dataset_split = combined_dataset.train_test_split(test_size=0.1)
train_data = dataset_split["train"]
eval_data = dataset_split["test"]

# Load the GPT-2 tokenizer and model for sequence classification
tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
model = GPT2ForSequenceClassification.from_pretrained("gpt2", num_labels=1)

# Add a pad token to the tokenizer and model
tokenizer.pad_token = tokenizer.eos_token
model.config.pad_token_id = tokenizer.eos_token_id

# Move the model to the GPU
model.to(device)

# Tokenize the data
def tokenize_data(example):
    encoding = tokenizer(
        example['text'],
        truncation=True,
        padding="max_length",
        max_length=512,
    )
    encoding['labels'] = example['label']
    return encoding


# Apply tokenization to train and validation sets
train_data = train_data.map(tokenize_data)
eval_data = eval_data.map(tokenize_data)

train_data.set_format(type='torch', columns=['input_ids', 'attention_mask', 'labels'])
eval_data.set_format(type='torch', columns=['input_ids', 'attention_mask', 'labels'])

# Define training arguments
training_args = TrainingArguments(
    output_dir="../gpt2-similarity",
    evaluation_strategy="epoch",
    learning_rate=2e-5,
    per_device_train_batch_size=4,
    num_train_epochs=3,
    weight_decay=0.01,
)

# Define the trainer
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=train_data,
    eval_dataset=eval_data,
)

# Fine-tune the model
trainer.train()

# Save the model and tokenizer
model.save_pretrained("./gpt2-similarity")
tokenizer.save_pretrained("./gpt2-similarity")


# Testing the fine-tuned model with a custom prompt
def calculate_similarity(prompt, ground_truth_text):
    # Tokenize inputs and move them to the GPU if available
    inputs = tokenizer(prompt + " " + ground_truth_text, return_tensors="pt", truncation=True, padding="max_length",
                       max_length=512).to(device)

    # Run inference
    with torch.no_grad():
        outputs = model(**inputs)
        similarity_score = torch.sigmoid(outputs.logits).item()  # Convert logits to probability
    return similarity_score


# Example usage
prompt = "This is a new article about Romanian politics."
ground_truth_text = "Some fake news headline and body text."
similarity = calculate_similarity(prompt, ground_truth_text)
print(f"Similarity score between prompt and ground truth: {similarity:.2f}")
