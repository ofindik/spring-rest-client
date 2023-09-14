curl -XPOST http://localhost:8080/api/posts \
    --header 'Content-Type: application/json' \
    --data \
'{
     "title": "Test Title",
     "body": "Test Body",
     "userId": 1
}'

curl -XPUT http://localhost:8080/api/posts/1 \
    --header 'Content-Type: application/json' \
    --data \
'{
     "title": "Test Title Updated",
     "body": "Test Body Updated",
     "userId": 1
}'

curl -XDELETE http://localhost:8080/api/posts/1

curl -XGET http://localhost:8080/api/posts/1  | json_pp

curl -XGET http://localhost:8080/api/posts | json_pp
