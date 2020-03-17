from django.urls import path
from . import views

urlpatterns = [
    path('', views.LoginPageView.as_view(), name='login'), # Login url
    path('signup/', views.SignUpPageView.as_view(), name='signup'), # SignUp url
    path('forgotpassword/', views.ForgotPasswordPageView.as_view(), name='forgotpassword'), # Forgot Password url
    path('user/create/', views.create_user, name='create_user'), # user cration api
    path('loginStudent/', views.login_student, name='student_login'), # student login api
    path('loginAdmin/', views.login_admin, name='admin_login'), # admin login api
    path('admin/dashboard/', views.AdminDashboardPageView.as_view(), name='Admindashboard'), # Admin dashboard url
    path('admin/manageAdmin/', views.manageAdminPageView.as_view(), name='manageAdmin'), # manageAdmin url
    path('admin/getAllAdmins/', views.get_all_admins, name='getAllAdmins'), # Get All Admin api
    #path('student/dashboard/', views.StudentDashboardPageView.as_view(), name='dashboard'), # dashboard url
    #path('adminSports/', views.SportsPageView.as_view(), name='sports'), # sports url
]
