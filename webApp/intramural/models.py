from django.db import models

# Create your models here.

class Signup(models.Model):
    firstName = models.CharField(max_length=100, null=False)
    lastName = models.CharField(max_length=100) 
    email = models.CharField(primary_key=True,max_length=100)
    password = models.CharField(max_length=100)

    def __str__(self):
        return self.firstName
   
