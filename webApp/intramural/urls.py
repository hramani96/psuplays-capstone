from django.urls import path
from . import views

urlpatterns = [
    path('', views.LoginPageView.as_view(), name='login'), # Login url
    path('signup/', views.SignUpPageView.as_view(), name='signup'), # SignUp url
    path('forgotpassword/', views.ForgotPasswordPageView.as_view(), name='forgotpassword'), # Forgot Password url
    path('user/create/', views.create_user, name='create_user'), # user cration api
    path('login/', views.login, name='login'), # login api
    path('dashboard/', views.DashboardPageView.as_view(), name='dashboard'), # login api
]
