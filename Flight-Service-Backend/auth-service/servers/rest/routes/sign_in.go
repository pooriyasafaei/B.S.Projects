package routes

import (
	"auth-service/controller"
	"github.com/gin-gonic/gin"
	"net/http"
)

func SignIn(c *gin.Context) {
	var model controller.SignInModel
	if err := c.ShouldBindJSON(&model); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	tokenModel, err := controller.SignIn(&model)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, tokenModel)
}
