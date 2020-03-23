from django.shortcuts import render
from django.views.generic import TemplateView # Import TemplateView
from django.http import HttpResponse
from .models import Users
from django.core import serializers
import json
from .helpers import is_empty
from django.views.decorators.csrf import csrf_exempt

class LoginPageView(TemplateView):
    template_name = "login.html"

class SignUpPageView(TemplateView):
    template_name = "signUp.html"

class ForgotPasswordPageView(TemplateView):
    template_name = "forgotPassword.html"

class AdminDashboardPageView(TemplateView):
    template_name = "adminDashboard.html"

class manageAdminPageView(TemplateView):
    template_name = "manageAdmin.html"

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
