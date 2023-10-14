package routes

import (
	"auth-service/controller"
	"auth-service/utils/rest"
	"github.com/gin-gonic/gin"
	"net/http"
)

func UserInfo(c *gin.Context) {

	token := rest.GetTokenFromCtxHeader(c)

	userInfo, err := controller.UserInfo(token)

	if err != nil {
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, userInfo)
}
