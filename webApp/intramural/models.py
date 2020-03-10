from django.db import models
from django.core.validators import MaxValueValidator

# Create your models here.

class Users(models.Model):
    first_name = models.CharField(max_length=50, default='', null=False)
    last_name = models.CharField(max_length=50, default='', null=False) 
    email = models.CharField(max_length=50, unique=True, default='', null=False)
    password = models.CharField(max_length=50, default='', null=False)

    def __str__(self):
        return self.first_name
   

class User_medical_info(models.Model):
    student_id = models.ForeignKey(Users, on_delete=models.CASCADE)
    birth_date = models.DateField(null=False)
    height = models.IntegerField(validators=[MaxValueValidator(300)], default=0, null=False)
    weight = models.IntegerField(validators=[MaxValueValidator(600)], default=0, null=False)
    description = models.TextField(max_length=200, default='', null=False)
    emerg_cont_name = models.TextField(max_length=50, default='', null=False)
    emerg_cont_number = models.CharField(max_length=10, default='', null=False)

    def __str__(self):
        return self.id
