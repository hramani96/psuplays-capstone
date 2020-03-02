from django.db import models

# Create your models here.

class Users(models.Model):
    first_name = models.CharField(max_length=50, default='', null=False)
    last_name = models.CharField(max_length=50, default='', null=False) 
    email = models.CharField(max_length=50, unique=True, default='', null=False)
    password = models.CharField(max_length=50, default='', null=False)

    def __str__(self):
        return self.first_name
   
