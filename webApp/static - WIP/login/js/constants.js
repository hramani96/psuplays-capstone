var app = angular.module('signup', []);

.constant('AUTH_EVENTS', {
	notAuthenticated: 'auth-not-authenticated;,
	notAuthorized: 'auth-not-authorized'
})

.constant('USER_ROLES', {
	admin: 'admin_role',
	student: 'student_role',
	public: 'public_role'
});
