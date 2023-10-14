package redis

import (
	"context"
	"github.com/go-redis/redis/v9"
	"log"
	"os"
	"strconv"
)

var Ctx = context.Background()

var Client *redis.Client

func InitRedis() {
	redisAddress := os.Getenv("REDIS_ADDRESS")
	redisPassword := os.Getenv("REDIS_PASS}")
	redisDB, err := strconv.Atoi(os.Getenv("REDIS_DB"))
	if err != nil {
		log.Fatal("can not parse redis db from .env", err)
	}
	Client = redis.NewClient(&redis.Options{
		Addr:     redisAddress,
		Password: redisPassword,
		DB:       redisDB,
	})
}
