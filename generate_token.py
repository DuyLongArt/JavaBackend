import hmac
import hashlib
import base64
import json
import time

secret = "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
header = {"alg": "HS256", "typ": "JWT"}
now = int(time.time())
payload = {
    "sub": "mockUser",
    "role": "USER",
    "iat": now,
    "exp": now + 86400,
    "device": "mockDevice",
    "ip": "127.0.0.1"
}

def base64url_encode(data):
    return base64.urlsafe_b64encode(data).rstrip(b'=')

encoded_header = base64url_encode(json.dumps(header).encode('utf-8'))
encoded_payload = base64url_encode(json.dumps(payload).encode('utf-8'))

signature_input = encoded_header + b"." + encoded_payload
signature = hmac.new(secret.encode('utf-8'), signature_input, hashlib.sha256).digest()
encoded_signature = base64url_encode(signature)

jwt = (signature_input + b"." + encoded_signature).decode('utf-8')
print("Generated Mock Token:")
print(jwt)
