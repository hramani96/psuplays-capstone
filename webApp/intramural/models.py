from django.db import models
from django.core.validators import MaxValueValidator

# Create your models here.

class Users(models.Model):
    first_name = models.CharField(max_length=50, default='', null=False)
    last_name = models.CharField(max_length=50, default='', null=False) 
    email = models.CharField(max_length=50, unique=True, default='', null=False)
    password = models.CharField(max_length=50, default='', null=False)
    role = models.CharField(max_length=7, default='Student', null=False)

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


class Teams(models.Model):
	name = models.CharField(max_length=50, default='', null=False)
	description = models.TextField(max_length=200, default='', null=False)
	sport = models.CharField(max_length=50, default='', null=False)
	accepted = models.CharField(max_length=1, default='N', null=False)
	
	def __str__(self):
		return self.name


class Sport(models.Model):
    name = models.CharField(max_length=50, unique=True, default='', null=False)
    max_teams_capacity = models.IntegerField(validators=[MaxValueValidator(12)], default=0, null=False)
    max_players_capacity = models.IntegerField(validators=[MaxValueValidator(12)], default=0, null=False)

    def __str__(self):
        return self.name

class Games(models.Model):
    team_1 = models.CharField(max_length=50, default='', null=False)
    score_1 = models.IntegerField(validators=[MaxValueValidator(100)], default=0, null=False)
    team_2 = models.CharField(max_length=50, default='', null=False)
    score_2 = models.IntegerField(validators=[MaxValueValidator(100)], default=0, null=False)
    active = models.CharField(max_length=1, default='Y', null=False)
	
    def __str__(self):
        return self.team_1
