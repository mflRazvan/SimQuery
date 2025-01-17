---
tags:
- sentence-transformers
- sentence-similarity
- feature-extraction
- generated_from_trainer
- dataset_size:4000
- loss:CosineSimilarityLoss
base_model: sentence-transformers/stsb-bert-large
widget:
- source_sentence: Someone is cleaning a animal.
  sentences:
  - A woman is cleaning a shrimp.
  - The white dog is running along the beach.
  - Two males, one in red and one in white playing a basketball game.
- source_sentence: Domestic cat laying on back of cat litter.
  sentences:
  - I don't think what she's saying is that controversial.
  - Greek PM calls for May 6 general election
  - Tan domestic cat under black tarp.
- source_sentence: The stock rose $2.11, or about 11 percent, to close on Friday at
    $21.51 on the New York Stock Exchange.
  sentences:
  - The physical evidence does not support your claim.
  - PG&E Corp. shares jumped $1.63 or 8 percent to $21.03 on the New York Stock Exchange
    on Friday.
  - china has stated that the united states pressure on china to stop and search north
    korean ships could trigger military clashes.
- source_sentence: ingo kober stated there is larger interest in intellectual property
    rights now than 20 or 30 years ago.
  sentences:
  - You seem as irritated as Henry.
  - The ingredients of such a polypill would contain aspirin, a cholesterol-lowering
    statin, three blood pressure-lowering agents in half dose, and folic acid.
  - ingo kober stated that some nations had stronger intellectual property rights
    than others.
- source_sentence: A man in a white suit walks down the street holding a newspaper.
  sentences:
  - A man in a white suit walks outside carrying a newspaper.
  - A woman is applying eye makeup.
  - Obama is Bush's fault.
pipeline_tag: sentence-similarity
library_name: sentence-transformers
metrics:
- pearson_cosine
- spearman_cosine
model-index:
- name: SentenceTransformer based on sentence-transformers/stsb-bert-large
  results:
  - task:
      type: semantic-similarity
      name: Semantic Similarity
    dataset:
      name: Unknown
      type: unknown
    metrics:
    - type: pearson_cosine
      value: 0.8796409560583546
      name: Pearson Cosine
    - type: spearman_cosine
      value: 0.8815620674501248
      name: Spearman Cosine
---

# SentenceTransformer based on sentence-transformers/stsb-bert-large

This is a [sentence-transformers](https://www.SBERT.net) model finetuned from [sentence-transformers/stsb-bert-large](https://huggingface.co/sentence-transformers/stsb-bert-large). It maps sentences & paragraphs to a 1024-dimensional dense vector space and can be used for semantic textual similarity, semantic search, paraphrase mining, text classification, clustering, and more.

## Model Details

### Model Description
- **Model Type:** Sentence Transformer
- **Base model:** [sentence-transformers/stsb-bert-large](https://huggingface.co/sentence-transformers/stsb-bert-large) <!-- at revision cc28955ccd980726b037449c473813587902123f -->
- **Maximum Sequence Length:** 128 tokens
- **Output Dimensionality:** 1024 dimensions
- **Similarity Function:** Cosine Similarity
<!-- - **Training Dataset:** Unknown -->
<!-- - **Language:** Unknown -->
<!-- - **License:** Unknown -->

### Model Sources

- **Documentation:** [Sentence Transformers Documentation](https://sbert.net)
- **Repository:** [Sentence Transformers on GitHub](https://github.com/UKPLab/sentence-transformers)
- **Hugging Face:** [Sentence Transformers on Hugging Face](https://huggingface.co/models?library=sentence-transformers)

### Full Model Architecture

```
SentenceTransformer(
  (0): Transformer({'max_seq_length': 128, 'do_lower_case': False}) with Transformer model: BertModel 
  (1): Pooling({'word_embedding_dimension': 1024, 'pooling_mode_cls_token': False, 'pooling_mode_mean_tokens': True, 'pooling_mode_max_tokens': False, 'pooling_mode_mean_sqrt_len_tokens': False, 'pooling_mode_weightedmean_tokens': False, 'pooling_mode_lasttoken': False, 'include_prompt': True})
)
```

## Usage

### Direct Usage (Sentence Transformers)

First install the Sentence Transformers library:

```bash
pip install -U sentence-transformers
```

Then you can load this model and run inference.
```python
from sentence_transformers import SentenceTransformer

# Download from the 🤗 Hub
model = SentenceTransformer("sentence_transformers_model_id")
# Run inference
sentences = [
    'A man in a white suit walks down the street holding a newspaper.',
    'A man in a white suit walks outside carrying a newspaper.',
    "Obama is Bush's fault.",
]
embeddings = model.encode(sentences)
print(embeddings.shape)
# [3, 1024]

# Get the similarity scores for the embeddings
similarities = model.similarity(embeddings, embeddings)
print(similarities.shape)
# [3, 3]
```

<!--
### Direct Usage (Transformers)

<details><summary>Click to see the direct usage in Transformers</summary>

</details>
-->

<!--
### Downstream Usage (Sentence Transformers)

You can finetune this model on your own dataset.

<details><summary>Click to expand</summary>

</details>
-->

<!--
### Out-of-Scope Use

*List how the model may foreseeably be misused and address what users ought not to do with the model.*
-->

## Evaluation

### Metrics

#### Semantic Similarity

* Evaluated with [<code>EmbeddingSimilarityEvaluator</code>](https://sbert.net/docs/package_reference/sentence_transformer/evaluation.html#sentence_transformers.evaluation.EmbeddingSimilarityEvaluator)

| Metric              | Value      |
|:--------------------|:-----------|
| pearson_cosine      | 0.8796     |
| **spearman_cosine** | **0.8816** |

<!--
## Bias, Risks and Limitations

*What are the known or foreseeable issues stemming from this model? You could also flag here known failure cases or weaknesses of the model.*
-->

<!--
### Recommendations

*What are recommendations with respect to the foreseeable issues? For example, filtering explicit content.*
-->

## Training Details

### Training Dataset

#### Unnamed Dataset


* Size: 4,000 training samples
* Columns: <code>sentence_0</code>, <code>sentence_1</code>, and <code>label</code>
* Approximate statistics based on the first 1000 samples:
  |         | sentence_0                                                                        | sentence_1                                                                        | label                                                          |
  |:--------|:----------------------------------------------------------------------------------|:----------------------------------------------------------------------------------|:---------------------------------------------------------------|
  | type    | string                                                                            | string                                                                            | float                                                          |
  | details | <ul><li>min: 6 tokens</li><li>mean: 15.69 tokens</li><li>max: 53 tokens</li></ul> | <ul><li>min: 5 tokens</li><li>mean: 15.75 tokens</li><li>max: 49 tokens</li></ul> | <ul><li>min: 0.0</li><li>mean: 0.52</li><li>max: 1.0</li></ul> |
* Samples:
  | sentence_0                                                          | sentence_1                                                                   | label                            |
  |:--------------------------------------------------------------------|:-----------------------------------------------------------------------------|:---------------------------------|
  | <code>A white and brown dog playing with a black toy.</code>        | <code>A white and brown dog is playing tug-o-war.</code>                     | <code>0.6800000190734863</code>  |
  | <code>I have no problem with those that have money!</code>          | <code>I have many problems with these men, way to many to state here!</code> | <code>0.04000000059604645</code> |
  | <code>Yeah, I don't think this is that controversial either.</code> | <code>I don't think what she's saying is that controversial.</code>          | <code>0.7599999904632568</code>  |
* Loss: [<code>CosineSimilarityLoss</code>](https://sbert.net/docs/package_reference/sentence_transformer/losses.html#cosinesimilarityloss) with these parameters:
  ```json
  {
      "loss_fct": "torch.nn.modules.loss.MSELoss"
  }
  ```

### Training Hyperparameters
#### Non-Default Hyperparameters

- `per_device_train_batch_size`: 16
- `per_device_eval_batch_size`: 16
- `num_train_epochs`: 10
- `multi_dataset_batch_sampler`: round_robin

#### All Hyperparameters
<details><summary>Click to expand</summary>

- `overwrite_output_dir`: False
- `do_predict`: False
- `eval_strategy`: no
- `prediction_loss_only`: True
- `per_device_train_batch_size`: 16
- `per_device_eval_batch_size`: 16
- `per_gpu_train_batch_size`: None
- `per_gpu_eval_batch_size`: None
- `gradient_accumulation_steps`: 1
- `eval_accumulation_steps`: None
- `torch_empty_cache_steps`: None
- `learning_rate`: 5e-05
- `weight_decay`: 0.0
- `adam_beta1`: 0.9
- `adam_beta2`: 0.999
- `adam_epsilon`: 1e-08
- `max_grad_norm`: 1
- `num_train_epochs`: 10
- `max_steps`: -1
- `lr_scheduler_type`: linear
- `lr_scheduler_kwargs`: {}
- `warmup_ratio`: 0.0
- `warmup_steps`: 0
- `log_level`: passive
- `log_level_replica`: warning
- `log_on_each_node`: True
- `logging_nan_inf_filter`: True
- `save_safetensors`: True
- `save_on_each_node`: False
- `save_only_model`: False
- `restore_callback_states_from_checkpoint`: False
- `no_cuda`: False
- `use_cpu`: False
- `use_mps_device`: False
- `seed`: 42
- `data_seed`: None
- `jit_mode_eval`: False
- `use_ipex`: False
- `bf16`: False
- `fp16`: False
- `fp16_opt_level`: O1
- `half_precision_backend`: auto
- `bf16_full_eval`: False
- `fp16_full_eval`: False
- `tf32`: None
- `local_rank`: 0
- `ddp_backend`: None
- `tpu_num_cores`: None
- `tpu_metrics_debug`: False
- `debug`: []
- `dataloader_drop_last`: False
- `dataloader_num_workers`: 0
- `dataloader_prefetch_factor`: None
- `past_index`: -1
- `disable_tqdm`: False
- `remove_unused_columns`: True
- `label_names`: None
- `load_best_model_at_end`: False
- `ignore_data_skip`: False
- `fsdp`: []
- `fsdp_min_num_params`: 0
- `fsdp_config`: {'min_num_params': 0, 'xla': False, 'xla_fsdp_v2': False, 'xla_fsdp_grad_ckpt': False}
- `fsdp_transformer_layer_cls_to_wrap`: None
- `accelerator_config`: {'split_batches': False, 'dispatch_batches': None, 'even_batches': True, 'use_seedable_sampler': True, 'non_blocking': False, 'gradient_accumulation_kwargs': None}
- `deepspeed`: None
- `label_smoothing_factor`: 0.0
- `optim`: adamw_torch
- `optim_args`: None
- `adafactor`: False
- `group_by_length`: False
- `length_column_name`: length
- `ddp_find_unused_parameters`: None
- `ddp_bucket_cap_mb`: None
- `ddp_broadcast_buffers`: False
- `dataloader_pin_memory`: True
- `dataloader_persistent_workers`: False
- `skip_memory_metrics`: True
- `use_legacy_prediction_loop`: False
- `push_to_hub`: False
- `resume_from_checkpoint`: None
- `hub_model_id`: None
- `hub_strategy`: every_save
- `hub_private_repo`: None
- `hub_always_push`: False
- `gradient_checkpointing`: False
- `gradient_checkpointing_kwargs`: None
- `include_inputs_for_metrics`: False
- `include_for_metrics`: []
- `eval_do_concat_batches`: True
- `fp16_backend`: auto
- `push_to_hub_model_id`: None
- `push_to_hub_organization`: None
- `mp_parameters`: 
- `auto_find_batch_size`: False
- `full_determinism`: False
- `torchdynamo`: None
- `ray_scope`: last
- `ddp_timeout`: 1800
- `torch_compile`: False
- `torch_compile_backend`: None
- `torch_compile_mode`: None
- `dispatch_batches`: None
- `split_batches`: None
- `include_tokens_per_second`: False
- `include_num_input_tokens_seen`: False
- `neftune_noise_alpha`: None
- `optim_target_modules`: None
- `batch_eval_metrics`: False
- `eval_on_start`: False
- `use_liger_kernel`: False
- `eval_use_gather_object`: False
- `average_tokens_across_devices`: False
- `prompts`: None
- `batch_sampler`: batch_sampler
- `multi_dataset_batch_sampler`: round_robin

</details>

### Training Logs
| Epoch | Step | Training Loss | spearman_cosine |
|:-----:|:----:|:-------------:|:---------------:|
| 0     | 0    | -             | 0.8816          |
| 2.0   | 500  | 0.0066        | -               |
| 4.0   | 1000 | 0.0047        | -               |
| 6.0   | 1500 | 0.0024        | -               |
| 8.0   | 2000 | 0.0016        | -               |
| 10.0  | 2500 | 0.0012        | -               |


### Framework Versions
- Python: 3.11.0
- Sentence Transformers: 3.3.1
- Transformers: 4.47.0
- PyTorch: 2.5.1+cu118
- Accelerate: 1.2.1
- Datasets: 3.2.0
- Tokenizers: 0.21.0

## Citation

### BibTeX

#### Sentence Transformers
```bibtex
@inproceedings{reimers-2019-sentence-bert,
    title = "Sentence-BERT: Sentence Embeddings using Siamese BERT-Networks",
    author = "Reimers, Nils and Gurevych, Iryna",
    booktitle = "Proceedings of the 2019 Conference on Empirical Methods in Natural Language Processing",
    month = "11",
    year = "2019",
    publisher = "Association for Computational Linguistics",
    url = "https://arxiv.org/abs/1908.10084",
}
```

<!--
## Glossary

*Clearly define terms in order to be accessible across audiences.*
-->

<!--
## Model Card Authors

*Lists the people who create the model card, providing recognition and accountability for the detailed work that goes into its construction.*
-->

<!--
## Model Card Contact

*Provides a way for people who have updates to the Model Card, suggestions, or questions, to contact the Model Card authors.*
-->