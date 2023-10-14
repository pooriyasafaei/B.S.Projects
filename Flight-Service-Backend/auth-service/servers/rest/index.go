package rest

import (
	_const "auth-service/const"
	"auth-service/servers/rest/routes"
	"github.com/gin-gonic/gin"
	"log"
)

func StartServer() {
	r := gin.Default()

	r.GET("/ping", routes.Ping)
	r.POST("/signup", routes.Signup)
	r.POST("/sign-in", routes.SignIn)
	r.POST("/refresh-token", routes.RefreshToken)
	r.POST("/sign-out", routes.SignOut)
	r.GET("/user-info", routes.UserInfo)

	//if err := r.RunTLS(_const.Port, _const.ServerPemLocation, _const.ServerKeyPemLocation); err != nil {
	//	log.Fatal("can not start rest server ", err)
	//}
	if err := r.Run(_const.Port); err != nil {
		log.Fatal("can not start rest server ", err)
	}
}
