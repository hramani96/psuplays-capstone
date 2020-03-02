from django.shortcuts import render
from django.views.generic import TemplateView # Import TemplateView
from django.http import HttpResponse
from .models import Users
import json
from django.views.decorators.csrf import csrf_exempt

class LoginPageView(TemplateView):
    template_name = "login.html"

class SignUpPageView(TemplateView):
    template_name = "signUp.html"

class ForgotPasswordPageView(TemplateView):
    template_name = "forgotPassword.html"

@csrf_exempt
def add_student(request):
    req_data = json.loads(request.body)
    try:
        data = {}
        first_name = req_data["first_name"]
        last_name = req_data["last_name"]
        email = req_data["email"]
        password = req_data["password"]
        password_conf = req_data["password_conf"]

        # TODO: Validate the data here

        # first_name Empty Field validation
        if first_name == '':
            data["status"] = "failure"
            data["reason"] = "Please Enter First Name"
            return HttpResponse(json.dumps(data), status=500)
        
        # last_name Empty Field validation
        if last_name == '':
            data["status"] = "failure"
            data["reason"] = "Please Enter Last Name"
            return HttpResponse(json.dumps(data), status=500)

        # email Empty Field validation
        if email == '':
            data["status"] = "failure"
            data["reason"] = "Please Enter Valid Email"
            return HttpResponse(json.dumps(data), status=500)

        # email compatible validation
        if email.endswith('psu.edu') == False:
            data["status"] = "failure"
            data["reason"] = "Email should be in format of abc1234@psu.edu"
            return HttpResponse(json.dumps(data), status=500)

        # password Empty Field validation
        if password == '':
            data["status"] = "failure"
            data["reason"] = "Please Enter Password"
            return HttpResponse(json.dumps(data), status=500)

        # password_conf Empty validation
        if password_conf == '':
            data["status"] = "failure"
            data["reason"] = "Please Fill up Re-enter Password Field"
            return HttpResponse(json.dumps(data), status=500)

        # emaill already exists validation
        if Users.objects.filter(email=email).count() > 0:
            data["status"] = "failure"
            data["reason"] = "User with this email already exists"
            return HttpResponse(json.dumps(data), status=500)

        # passwords did not match validation
        if password_conf != password:
            data["status"] = "failure"
            data["reason"] = "Passwords did not match"
            return HttpResponse(json.dumps(data), status=500)

        user = Users(first_name=first_name,last_name=last_name,email=email,password=password)
        user.save()
        data["status"] = "success"
        return HttpResponse(json.dumps(data), status=200)
    except Exception as e:
        print("[EXCEPTION][ADD_STUDENT] ::: {}".format(e))
        data["status"] = "failure"
        data["reason"] = "Because you made a stupid mistake"
        return HttpResponse(json.dumps(data), status=500)
