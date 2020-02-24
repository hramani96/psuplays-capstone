from django.urls import path
from . import views

urlpatterns = [
    path('', views.LoginPageView.as_view(), name='login'), # Login url
    path('signup/', views.SignUpPageView.as_view(), name='signup'), # SignUp url
    path('forgotpassword/', views.ForgotPasswordPageView.as_view(), name='forgotpassword'), # Forgot Password url
    path('add_student/', views.add_student, name='add_student'), # add_student url
]
