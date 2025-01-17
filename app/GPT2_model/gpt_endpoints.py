from transformers import GPT2Tokenizer, GPT2ForSequenceClassification
import torch
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

class GPT2Similarity:
    def __init__(self, model_path: str, ground_truth_path: str):
        """
        Initialize the GPT2 similarity model with a specified model path.
        """
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model = GPT2ForSequenceClassification.from_pretrained(model_path).to(self.device)
        self.tokenizer = GPT2Tokenizer.from_pretrained(model_path)
        self.model.eval()
        self.ground_truth_path = ground_truth_path
        self.read_ground_truth()

    def read_ground_truth(self):
        """
        Read the ground truth for similarity evaluation.
        """
        with open(self.ground_truth_path, "r") as file:
            self.ground_truth = file.read()

    def get_similarity(self, prompt: str) -> float:
        """
        Calculate similarity between the prompt and the ground truth.
        """
        # Combine the prompt and ground truth for input
        inputs_text = f"{prompt} => {self.ground_truth}"
        tokenized_inputs = self.tokenizer(
            inputs_text, 
            return_tensors="pt", 
            padding=True, 
            truncation=True, 
            max_length=512
        )
        tokenized_inputs = {key: val.to(self.device) for key, val in tokenized_inputs.items()}

        with torch.no_grad():
            outputs = self.model(**tokenized_inputs)
            similarity_score = torch.sigmoid(outputs.logits).item()

        return similarity_score


# API Implementation Using FastAPI
app = FastAPI()

# Initialize the model (provide the path to your pretrained model)
MODEL_PATH = "./gpt2-news-category"
GROUND_TRUTH = "ground-truth.txt"
similarity_model = GPT2Similarity(MODEL_PATH, GROUND_TRUTH)


class SimilarityRequest(BaseModel):
    """
    Request model for similarity evaluation.
    """
    prompt: str



@app.post("/get-similarity/")
def get_similarity(request: SimilarityRequest):
    """
    Endpoint to calculate similarity between the given prompt and the ground truth.
    """
    try:
        similarity_score = similarity_model.get_similarity(request.prompt)
        return {"similarity_score": similarity_score}
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
