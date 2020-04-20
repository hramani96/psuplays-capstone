from django.shortcuts import render
from django.views.generic import TemplateView # Import TemplateView
from django.http import HttpResponse
from .models import Users
from .models import Teams
from .models import Sport
from .models import Games
from django.core import serializers
import json
from .helpers import is_empty
from django.views.decorators.csrf import csrf_exempt

# Login Pages

class LoginPageView(TemplateView):
    template_name = "login.html"

class SignUpPageView(TemplateView):
    template_name = "signUp.html"

class ForgotPasswordPageView(TemplateView):
    template_name = "forgotPassword.html"

# Admin Pages

class AdminDashboardPageView(TemplateView):
    template_name = "adminDashboard.html"

class manageAdminPageView(TemplateView):
    template_name = "manageAdmin.html"
    
class ModifyAdmin(TemplateView):
    template_name = "modifyAdmin.html"

class TeamApprovePageView(TemplateView):
    template_name ="teamApprove.html"
	
class AdminTeamsPageView(TemplateView):
	template_name ="adminTeams.html"

class adminSportPageView(TemplateView):
    template_name = "adminSports.html"

class Generate_SchedulePageView(TemplateView):
	template_name ="generateSchedule.html"

# Student Pages

class StudentDashboardPageView(TemplateView):
    template_name = "studentDashboard.html"

class StudentTeamPageView(TemplateView):
    template_name = "studentTeam.html"

class StudentCreateTeamPageView(TemplateView):
    template_name = "createTeam.html"

class StudentTeamInfoPageView(TemplateView):
    template_name = "studentTeamInfo.html"

# General Pages

class Leaderboard(TemplateView):
    template_name = "leaderboard.html"

# APIs

@csrf_exempt
def create_user(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        first_name = req_data["first_name"]
        last_name = req_data["last_name"]
        email = req_data["email"]
        password = req_data["password"]
        password_conf = req_data["password_conf"]
        role = req_data["role"]

        # Validate the data here
        if is_empty([first_name, last_name, email,
            password, password_conf, role]):
            error = "Required fields cannot be empty"

        # email compatible validation
        if email.endswith('@psu.edu') == False:
            error = "Improper email format"
            return HttpResponse(json.dumps(data), status=500)

        # emaill already exists validation
        if Users.objects.filter(email=email).count() > 0:
            error = "User already exists"

        # passwords did not match validation
        if password_conf != password:
            error = "Passwords do not match"

        # role is not Student validation
        #if role != "Student":
        #    error = "Do not play with me!"

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)


        user = Users(first_name=first_name,last_name=last_name,email=email,password=password,role=role)
        user.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][CREATE_USER] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def login_student(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data={}
        email = req_data["email"]
        password = req_data["password"]

        # Email validation
        if  Users.objects.filter(email=email).count() <= 0:
            error = "Incorrect Username/Password"

        # password validation
        elif password != Users.objects.filter(email=email).get().password:
            error = "Incorrect Username/Password"

        # Role validation (Making sure its Student)
        elif Users.objects.filter(email=email).get().role != "Student":
            error = "You are not a Student!"

        else:
            error = None

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][dashboard] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def login_admin(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data={}
        email = req_data["email"]
        password = req_data["password"]

        # Email validation
        if  Users.objects.filter(email=email).count() <= 0:
            error = "Incorrect Username/Password"

        # password validation
        elif password != Users.objects.filter(email=email).get().password:
            error = "Incorrect Username/Password"

        # Role validation (Making sure its Admin)
        elif Users.objects.filter(email=email).get().role != "Admin":
            error = "You are not an Admin!"

        else:
            error = None

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][dashboard] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def get_all_admins(request):
    try:
        data={}
        admins = Users.objects.filter(role='Admin').values("id", "first_name", "last_name", "email", "role")
        data["status"] = "success"
        data["admins"] = list(admins)
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][get_all_admins] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def remove_user(request):
    req_data = json.loads(request.body)
    try:
        data={}
        email = req_data["email"]
        Users.objects.filter(email=email).delete()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][dashboard] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def show_user(request):
    req_data = json.loads(request.body)
    try:
        data={}
        email = req_data["email"]
        Users.objects.filter(email=email).delete()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][dashboard] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)
 

@csrf_exempt
def create_team(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        name = req_data["name"]
        description = req_data["description"]
        sport = Sport.objects.get(name=req_data["sport"].get('name'))
        accepted = req_data["accepted"]

        # Validate the data here
        if is_empty([name, description, sport, accepted]):
            error = "Required fields cannot be empty"

        # team name already exists validation
        if Teams.objects.filter(name=name).count() > 0:
            error = "Team name already exists"

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)

        team = Teams(name=name, description=description, sport=sport, accepted=accepted)
        team.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][CREATE_TEAM] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def create_sport(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        name = req_data["name"]
        max_teams_capacity = req_data["max_teams_capacity"]
        max_players_capacity = req_data["max_players_capacity"]

        # Validate the data here
        if is_empty([name, max_teams_capacity, max_players_capacity]):
            error = "Required fields cannot be empty"

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)

        sport = Sport(name=name,max_teams_capacity=max_teams_capacity,max_players_capacity=max_players_capacity)
        sport.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][CREATE_SPORT] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)
		
@csrf_exempt
def get_all_teams(request):
    try:
        data={}
        teams = Teams.objects.values("id", "name", "description", "sport__name", "accepted")
        data["status"] = "success"
        data["teams"] = list(teams)
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][get_all_teams] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def get_new_teams(request):
    try:
        data={}
        teams = Teams.objects.filter(accepted = '?').values("id", "name", "description", "sport__name", "accepted")
        data["status"] = "success"
        data["teams"] = list(teams)
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][get_all_teams] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def get_approved_teams(request):
    try:
        data={}
        teams = Teams.objects.filter(accepted = 'Y').values("id", "name", "description", "sport__name")
        data["status"] = "success"
        data["teams"] = list(teams)
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][get_all_teams] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def get_all_sports(request):
    try:
        data={}
        sports = Sport.objects.all().values("id", "name", "max_teams_capacity", "max_players_capacity")
        data["status"] = "success"
        data["sports"] = list(sports)
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][get_all_sports] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def approve_team(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        id = req_data["id"]
        name = req_data["name"]
        description = req_data["description"]
        sport = Sport.objects.get(name=req_data["sport__name"])
        team = Teams(id = id, name=name, description=description, sport=sport, accepted='Y')
        team.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][APPROVE_TEAM] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)


@csrf_exempt
def remove_sport(request):
    req_data = json.loads(request.body)
    try:
        data={}
        name = req_data["name"]
        Sport.objects.filter(name=name).delete()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][remove_sport] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)


@csrf_exempt
def deny_team(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        id = req_data["id"]
        name = req_data["name"]
        description = req_data["description"]
        sport = Sport.objects.get(name=req_data["sport__name"])
        team = Teams(id = id, name=name, description=description, sport=sport, accepted='N')
        team.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][APPROVE_TEAM] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)
@csrf_exempt
def create_game(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        team_1 = req_data["team_1"]
        score_1 = req_data["score_1"]
        team_2 = req_data["team_2"]
        score_2 = req_data["score_2"]
        active = req_data["active"]

        # Validate the data here
        if is_empty([team_1, score_1, team_2, score_2, active]):
            error = "Required fields cannot be empty"

        # team name already exists validation	needs reworked to make sure a team isnt playing twice at the same time
        #if Teams.objects.filter(name=name).count() > 0:
        #    error = "Team name already exists"

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)

        game = Games(team_1=team_1, score_1=score_1, team_2=team_2, score_2=score_2, active = "Y")
        game.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][CREATE_GAME] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)
		
@csrf_exempt
def update_score(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        id = req_data["id"]
        team_1 = req_data["team_1"]
        score_1 = req_data["score_1"]
        team_2 = req_data["team_2"]
        score_2 = req_data["score_2"]
        
		# Validate the data here
        if is_empty([id, team_1, score_1, team_2, score_2]):
            error = "Required fields cannot be empty"

        # team name already exists validation	needs reworked to make sure a team isnt playing twice at the same time
        #if Teams.objects.filter(name=name).count() > 0:
        #    error = "Team name already exists"

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)

        game = Games(id=id, team_1=team_1, score_1=score_1, team_2=team_2, score_2=score_2, active = 'Y')
        game.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][UPDATE_SCORE] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)
		
@csrf_exempt
def get_active_games(request):
    try:
        data={}
        games = Games.objects.filter(active = 'Y').values("id", "team_1", "score_1", "team_2", "score_2")
        data["status"] = "success"
        data["games"] = list(games)
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][get_active_games] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)

@csrf_exempt
def end_game(request):
    req_data = json.loads(request.body)
    try:
        error = None
        data = {}
        id= req_data["id"]
        team_1 = req_data["team_1"]
        score_1 = req_data["score_1"]
        team_2 = req_data["team_2"]
        score_2 = req_data["score_2"]

		# Validate the data here
        if is_empty([id, team_1, score_1, team_2, score_2]):
            error = "Required fields cannot be empty"

        # team name already exists validation	needs reworked to make sure a team isnt playing twice at the same time
        #if Teams.objects.filter(name=name).count() > 0:
        #    error = "Team name already exists"

        if error is not None:
            data["status"] = "failure"
            data["reason"] = error
            return HttpResponse(json.dumps(data), status=500)

        game = Games(id=id,team_1=team_1, score_1=score_1, team_2=team_2, score_2=score_2, active = 'N')
        game.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][END_GAME] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a mistake"
        return HttpResponse(json.dumps(data), status=500)
