package main

import (
	_const "auth-service/const"
	"auth-service/database"
	"auth-service/database/models"
	"auth-service/redis"
	"auth-service/servers/rest"
	"github.com/joho/godotenv"
	"log"
)

func init() {
	if err := godotenv.Load(); err != nil {
		log.Fatal("can not load .env file", err)
	}
	_const.InitConsts()
}

func main() {
	redis.InitRedis()
	if err := database.InitDB(); err != nil {
		log.Fatal("can not create db connection", err)
	}
	if err := models.MigrateDatabase(); err != nil {
		log.Fatal("DataBase: can not migrate database", err)
	}
	go rest.StartServer()
	// todo: add grpc.StartServer()
	select {}
}
