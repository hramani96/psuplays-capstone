from django.urls import path
from . import views

urlpatterns = [
    # Login    
    path('', views.LoginPageView.as_view(), name='login'), # Login url
    path('signup/', views.SignUpPageView.as_view(), name='signup'), # SignUp url
    path('forgotpassword/', views.ForgotPasswordPageView.as_view(), name='forgotpassword'), # Forgot Password url
    path('user/create/', views.create_user, name='create_user'), # user cration api
    path('user/remove/', views.remove_user, name='remove_user'), # user remove api
    path('loginStudent/', views.login_student, name='student_login'), # student login api
    path('loginAdmin/', views.login_admin, name='admin_login'), # admin login api

    # Admin
    path('admin/dashboard/', views.AdminDashboardPageView.as_view(), name='Admindashboard'), # Admin dashboard url
    path('admin/manageAdmin/', views.manageAdminPageView.as_view(), name='manageAdmin'), # manage Admin url
    path('admin/sports/', views.adminSportPageView.as_view(), name='adminSport'), # admin Sports url
    path('admin/teams/', views.AdminTeamsPageView.as_view(), name='Adminteams'), # Admin teams url
    path('admin/teams/teamApprove/', views.TeamApprovePageView.as_view(), name='teamApprove'), # Admin team approve url
    path('admin/generate_schedule/', views.Generate_SchedulePageView.as_view(), name='Genereate_Schedule'), # Generate Schedule url
    path('admin/getAllAdmins/', views.get_all_admins, name='getAllAdmins'), # Get All Admins api

    # Student
    path('student/dashboard/', views.StudentDashboardPageView.as_view(), name='Studentdashboard'), # Student dashboard url
    path('student/team/', views.StudentTeamPageView.as_view(), name='StudentTeam'), # Student Team url
    path('student/team/createTeam/', views.StudentCreateTeamPageView.as_view(), name='StudentCreateTeam'), # Student Create Team url
    path('student/team/teamInfo/', views.StudentTeamInfoPageView.as_view(), name='StudentTeamInfo'), # Student Team Info url
    path('sport/create/', views.create_sport, name='create_sport'), # sport cration api
    path('sport/remove/', views.remove_sport, name='remove_sport'), # sport remove api
    path('sport/getAllSports/', views.get_all_sports, name='getAllSports'), # Get All Sports api
    path('team/create/', views.create_team, name='create_team'), #create team api
    path('team/getAllTeams/', views.get_all_teams, name='getAllTeams'), #get all teams api
    path('team/getNewTeams/', views.get_new_teams, name='getNewTeams'), #get new teams api
    path('team/getApprovedTeams/', views.get_approved_teams, name='getApprovedTeams'), #get all approved teams api
    path('team/ApproveTeam/', views.approve_team, name='approveTeam'), #approve team api
    path('team/DenyTeam/', views.deny_team, name='denyTeam'), #deny team api
    # Score
	path('score/update/', views.update_score, name='updateScore'),
	path('score/createGame/', views.create_game, name='createGame'),
	path('score/getActiveGames/', views.get_active_games, name='getActiveGames'),
	path('score/endGame/', views.end_game, name='endGame'),
]
