import argparse
import requests
import json

server_default_url = "http://localhost:8000/get-similarity"

def main(server_url):
    """
    Runs the console application.

    Args:
        server_url: URL of the server to send HTTP requests to.
    """
    print(f"Server URL: {server_url}")
    print("Enter a prompt to calculate similarity or type 'exit' to quit.")

    while True:
        # Read user input
        prompt = input("Prompt: ").strip()
        if prompt.lower() == "exit":
            print("Exiting the application. Goodbye!")
            break

        # Create payload and make HTTP POST request
        try:
            payload = {"prompt": prompt}
            response = requests.post(server_url, json=payload)

            # Handle response
            if response.status_code == 200:
                similarity_score = response.json().get("similarity_score", None)
                if similarity_score is not None:
                    print(f"Similarity score: {similarity_score:.2f}")
                else:
                    print("Error: No similarity score returned by the server.")
            else:
                print(f"Error: Server returned status code {response.status_code}.")
        except requests.exceptions.RequestException as e:
            print(f"Error: Unable to connect to the server. Details: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Similarity calculation client.")
    parser.add_argument(
        "--server_url", type=str, default=server_default_url, help="URL of the server endpoint"
    )
    args = parser.parse_args()
    main(args.server_url)
