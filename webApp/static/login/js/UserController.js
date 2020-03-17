var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('UserController', function($http, $window) {

    var vm = this;

    console.log("tsging");

    vm.admins = []

    this.test = "abcd";
    vm.test1 = "abcefvd";
    vm.formInfo = {};

    vm.index = function() {
    }

    vm.show = function() {
    }

    vm.create = function() {
        // TODO:Validate the form data here.
        // 1. Check email format
        // 2. Check name
        // 3. password and confirm password must match
        //
        // Change the api to '/user/create/'. Then on creation check 
        // the type of user. i.e whether we are creating student or faculty
        $http.post('/user/create/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" Account has been created");
            $window.location.href='/';
        },
        function error(response) {
            console.log(response);
            toastr.warning("Failure : " + response.data.reason);
        });
    }

    vm.update = function() {
    }

    vm.delete = function() {
    }

    vm.login_student = function() {
        $http.post('/loginStudent/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" login successful");
            $window.location.href='/dashboard/';
        },
        function error(response) {
            console.log(response);
            toastr.warning("failure : " + response.data.reason);
        });
    }

    vm.login_admin = function() {
        $http.post('/loginAdmin/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" login successful");
            $window.location.href='/admin/dashboard/';
        },
        function error(response) {
            console.log(response);
            toastr.warning("failure : " + response.data.reason);
        });
    }

    vm.getAllAdmins = function() {
        $http.get('/admin/getAllAdmins')
        .then(function success(response) {
            vm.admins = response.data.admins;
        }, function error(response) {
            vm.admins = ["abcd"];
        });
    }
});
