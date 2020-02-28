from django.shortcuts import render
from django.views.generic import TemplateView # Import TemplateView
from django.http import HttpResponse
from .models import Signup
import json

class LoginPageView(TemplateView):
    template_name = "login.html"

class SignUpPageView(TemplateView):
    template_name = "signUp.html"

class ForgotPasswordPageView(TemplateView):
    template_name = "forgotPassword.html"

def add_student(request):
    req_data = json.loads(request.body)
    try:
        data = {}
        firstName = req_data["first_name"]
        lastName = req_data["last_name"]
        email = req_data["email"]
        password = req_data["password"]
        password_conf = req_data["passconf"]

        # TODO: Validate the data here

        if password_conf != password:
            data["status"] = "failure"
            data["reason"] = "Passwords did not match"
            return HttpResponse(json.dumps(data), status=500)

        signup = Signup(firstName=firstName,lastName=lastName,email=email,password=password)
        signup.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][ADD_STUDENT] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Passwords did not match"
        return HttpResponse(json.dumps(data), status=500)

