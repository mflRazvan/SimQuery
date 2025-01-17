from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import faiss
from sentence_transformers import SentenceTransformer

class SBERTSimilarity:
    def __init__(self, model_path: str, ground_truth_path: str):
        """
        Initialize the Sentence BERT similarity model with a specified model path.
        """
        self.model = SentenceTransformer(model_path)
        self.model.eval() # ???

        self.ground_truth = None
        self.ground_truth_path = ground_truth_path
        self.read_ground_truth()

    def read_ground_truth(self):
        """
        Read the ground truth for similarity evaluation.
        """
        with open(self.ground_truth_path, "r") as file:
            self.ground_truth = file.read()

    def search_top_k_sentences(self, prompt, k):
        """
        Highlights the top-k most similar sentences from the ground truth and also
         calculates the highest similarity score.

        Args:
            prompt: The input prompt (string).
            k: Number of top sentences to retrieve.

        Returns:
            max_similarity: The highest similarity score (float).
        """
        # Step 1: Split the ground truth into sentences
        sentences = self.ground_truth.split('.')
        sentences = [s.strip() for s in sentences if s.strip()]

        # Step 2: Embed the sentences and the prompt
        sentence_embeddings = self.model.encode(sentences, convert_to_tensor=False, normalize_embeddings=True)
        prompt_embedding = self.model.encode([prompt], convert_to_tensor=False, normalize_embeddings=True)

        # Step 3: Build the FAISS index
        dimension = sentence_embeddings.shape[1]
        index = faiss.IndexFlatIP(dimension)  # Inner product (cosine similarity since embeddings are normalized)
        index.add(sentence_embeddings)

        # Step 4: Perform the search for top-k most similar sentences
        similarity_scores, indices = index.search(prompt_embedding, k)
        similarity_scores = similarity_scores[0]

        # Step 5: Handle highlighting and display if requested
        max_similarity = max(similarity_scores)

        # Step 6: Return the highest similarity score
        return float(max_similarity)

    def get_similarity(self, prompt: str) -> float:
        """
        Calculate similarity between the prompt and the ground truth.
        """
        return self.search_top_k_sentences(prompt, k=1)


# API Implementation Using FastAPI
app = FastAPI()

# Initialize the model (provide the path to your pretrained model)
MODEL_PATH = "./fine_tuned_sbert_model"
GROUND_TRUTH = "ground-truth.txt"
similarity_model = SBERTSimilarity(MODEL_PATH, GROUND_TRUTH)


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
