# Auth-Service

simple go project for auth service in rest.

before running the project you need to add redis and postgres and other vars in <code>.env</code> file:

```
PORT=
DATABASE_USER=
DATABASE_PASS=
DATABASE_HOST=
DATABASE_PORT=
DATABASE_NAME=
PROJECT_SECRET=
REDIS_ADDRESS=
REDIS_PASS=
REDIS_DB=
```

- you need to set port like:<code>:8000</code>

after that you need to run project with these commands:

```bash
> go mod download
> go build
> ./auth-service
```

