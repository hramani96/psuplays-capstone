from django.shortcuts import render
from django.views.generic import TemplateView # Import TemplateView
from django.http import HttpResponse
from .models import Users
import json
from .helpers import is_empty
from django.views.decorators.csrf import csrf_exempt

class LoginPageView(TemplateView):
    template_name = "login.html"

class SignUpPageView(TemplateView):
    template_name = "signUp.html"

class ForgotPasswordPageView(TemplateView):
    template_name = "forgotPassword.html"

class DashboardPageView(TemplateView):
    template_name = "dashboard.html"

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
        if role != "Student":
            error = "Do not play with me!"

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
        data["reason"] = "Because you made a stupid mistake"
        return HttpResponse(json.dumps(data), status=500)

def login(request):
    try:
        req_data = json.loads(request.body)
        data={}
        email = req_data["email"]
        password = req_data["password"]

        if  Users.objects.filter(email=email).count() <= 0:
            data["status"] = "failure"
            data["reason"] = "Incorrect Username/Password"
            return HttpResponse(json.dumps(data), status=500)

        if password != Users.objects.filter(email=email).get().password:
            data["status"] = "failure"
            data["reason"] = "Incorrect Username/Password"
            return HttpResponse(json.dumps(data), status=500)

        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][dashboard] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a stupid mistake"
        return HttpResponse(json.dumps(data), status=500)

