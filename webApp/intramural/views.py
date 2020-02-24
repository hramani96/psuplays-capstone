from django.shortcuts import render
from django.views.generic import TemplateView # Import TemplateView
from .models import Signup

class LoginPageView(TemplateView):
    template_name = "login.html"

class SignUpPageView(TemplateView):
    template_name = "signUp.html"

class ForgotPasswordPageView(TemplateView):
    template_name = "forgotPassword.html"

def add_student(request):
    print("Form is submitted.")
    firstName = request.POST["first_name"]
    lastName = request.POST["last_name"]
    email = request.POST["email"]
    password = request.POST["password"]
    password_conf = request.POST["passconf"]

    if password_conf == password:
        print("Passwords matches.")
        signup = Signup(firstName=firstName,lastName=lastName,email=email,password=password)
        signup.save()

    return render(request,"login.html")
