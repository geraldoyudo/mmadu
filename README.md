# mmadu
This is a user management service that enables developers to focus on writing applications without worrying about where to store user information, user authentication and authorization flows and all that nonsense. This takes care of all that. Mmadu was coined from the igbo word "Mmadu" meaning people. 

# Steps to Run Mmadu Service 
- Create a folder (we will use mmadu as the created folder)
````
mkdir mmadu && cd mmadu
````
- Download the [docker compose file](https://raw.githubusercontent.com/geraldoyudo/mmadu/master/docker/docker-compose.yml)
````
wget https://raw.githubusercontent.com/geraldoyudo/mmadu/master/docker/docker-compose.yml
````
- Download the [default environment file](https://raw.githubusercontent.com/geraldoyudo/mmadu/master/docker/.env)
````
wget https://raw.githubusercontent.com/geraldoyudo/mmadu/master/docker/.env
````
- Make the necessary modifications to suite your configuration (you can use it as is)
- Start the services
````
docker-compose up
````

Go to documentation [here](https://geraldoyudo.github.io/mmadu/)
