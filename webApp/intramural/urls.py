from django.urls import path
from . import views

urlpatterns = [
    path('', views.LoginPageView.as_view(), name='login'), # Login url
    path('signup/', views.SignUpPageView.as_view(), name='signup'), # SignUp url
    path('forgotpassword/', views.ForgotPasswordPageView.as_view(), name='forgotpassword'), # Forgot Password url
    path('user/create/', views.create_user, name='create_user'), # user cration api
    path('user/remove/', views.remove_user, name='remove_user'), # user remove api
    path('loginStudent/', views.login_student, name='student_login'), # student login api
    path('loginAdmin/', views.login_admin, name='admin_login'), # admin login api
    path('admin/dashboard/', views.AdminDashboardPageView.as_view(), name='Admindashboard'), # Admin dashboard url
    path('admin/manageAdmin/', views.manageAdminPageView.as_view(), name='manageAdmin'), # manageAdmin url
    path('admin/getAllAdmins/', views.get_all_admins, name='getAllAdmins'), # Get All Admin api
    #path('student/dashboard/', views.StudentDashboardPageView.as_view(), name='dashboard'), # dashboard url
    #path('adminSports/', views.SportsPageView.as_view(), name='sports'), # sports url
    path('admin/teamApprove/', views.TeamApprovePageView.as_view(), name='teamApprove'),
	path('team/create/', views.create_team, name='create_team'), #create team api
	path('Teams', views.TeamsPageView.as_view(), name='Teams'), #student teams url
	path('team/getAllTeams/', views.get_all_teams, name='getAllTeams'), #get all teams api
	path('team/getNewTeams/', views.get_new_teams, name='getNewTeams'), #get new teams api
	path('team/getApprovedTeams/', views.get_approved_teams, name='getApprovedTeams'), #get all approved teams api
	path('team/ApproveTeam/', views.approve_team, name='approveTeam'), #approve team api
	path('team/DenyTeam/', views.deny_team, name='denyTeam'), #deny team api
	path('admin/teams/', views.AdminTeamsPageView.as_view(), name='Adminteams'), # Admin teams dashboard url
]
