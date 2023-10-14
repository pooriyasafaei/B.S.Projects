package rest

import "github.com/gin-gonic/gin"

func GetTokenFromCtxHeader(c *gin.Context) string {
	token := c.GetHeader("Authorization")
	// remove JWT from start: "JWT token" -> "token"
	if len(token) > 4 {
		token = token[4:]
	}
	return token
}
