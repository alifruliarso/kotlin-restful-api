# API SPEC

## Create Product

Request :
- Method : POST
- Endpoint : `/api/products`
- Body :

```json
{
    "id": "string,unique",
    "name": "string",
    "price": "long",
    "quantity": "integer",
    "createdAt": "date"
}
```

Response :
```json
{
  "code": "number",
  "status": "string",
  "data": {
    "id": "string,unique",
    "name": "string",
    "price": "long",
    "quantity": "integer",
    "createdAt": "date"
  }
}
```