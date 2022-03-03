cd docker

#Build docker image

docker build -t rt/rate-limit-app .

#Run 
docker run -p 8080:8080  rt/rate-limit-app

#Request to rate limited endpoint

curl -I localhost:8080/api/v1/rate-limit
