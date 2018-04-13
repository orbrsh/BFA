var bookApp = angular.module('bookApp', []);
bookApp.controller('myController', ['$scope', function ($scope) {
    $scope.hey = "hi";
}]);

bookApp.value('userData', {
    isLogged: false,
    loggedUser: "",
    adminLogged: false,
    getPurchasedBooksFunc: null, //? // TODO: check
    adminMode: null,
    purchasedBooks: [] // update at login
});

bookApp.controller('bookController', ['$scope', '$http', '$filter', 'userData',
    function ($scope, $http, $filter, userData) {
        $scope.booklistView = {
            All: false,
            Purchased: false,
            singleBook: false
        };
        $scope.bookUserData = userData; //isLogged, loggedUser, adminLogged

        var bookListInit = getAllBooks();
        $scope.booklistnames = [];

        function getAllBooks() {
            $http.get("BooksServlet/getAllBooks").then(function (response) {
                    var booklistAll = response.data;
                    //$scope.booklistView.All = true;
                    angular.forEach(booklistAll, function (item) {
                        this.push(item.Name);
                    }, $scope.booklistnames);
                    $scope.booklist = response.data;
                    return response.data;
                },
                function (response) {});
        }

        $scope.allbooks = function () {
            $scope.booklistView.All = true;
            $scope.booklistView.Purchased = false;
            $scope.booklistView.singleBook = false;
            //$scope.booklist = bookListInit;
            // $http.get("BooksServlet/getAllBooks").then(function (response) {
            //         $scope.booklist = response.data;
            //         $scope.booklistView.All = true;
            //     },
            //     function (response) {});
        };
        $scope.purchasedbooks = function () {
            //only logged
            if (!$scope.bookUserData.isLogged) {
                console.log("purchased books - only for logged user");
                return;
            }
            $http.get("BooksServlet/myBooks").then(function (response) {
                    //$scope.purchasedBooklist = response.data;
                    $scope.bookUserData.purchasedBooks = response.data;
                    $scope.userBoughtThisBook = function (bookname) {
                        if (($scope.bookUserData.isLogged === false) || angular.isUndefined(bookname))
                            return false;
                        var bookFound = $filter('filter')($scope.bookUserData.purchasedBooks, {
                            Name: bookname
                        }, true)[0];
                        if (bookFound) { // user bought it
                            return true;
                        } else
                            return false;
                        // $filter('filter')(myArray, {'id':73})
                        // if $scope.book.Name
                    };
                },
                function (response) {
                    // no books / not logged.
                    console.log("Not logged in");
                });
            $scope.booklistView.Purchased = true;
            $scope.booklistView.All = false;
            $scope.booklistView.singleBook = false;
        };

        userData.getPurchasedBooksFunc = $scope.purchasedbooks; // TODO: check

        $scope.userLikedThisBook = function (likesList) {
            if (($scope.bookUserData.isLogged === false) || angular.isUndefined(likesList))
                return false;
            var likeFound = $filter('filter')(likesList, {
                Username: $scope.bookUserData.loggedUser
            }, true)[0];
            if (likeFound) { // user bought it
                return true;
            } else
                return false;
            // $filter('filter')(myArray, {'id':73})
            // if $scope.book.Name
        };
        $scope.singlebook = function () {
            var chosenBook = $scope.chosenSingleBook;
            if (angular.isUndefined(chosenBook)) {
                $scope.singlebook = null;
                return;
            }
            //$scope.singleBook = $scope.booklist.get({Name: chosenBook});
            $scope.singleBook = $filter('filter')($scope.booklist, {
                Name: chosenBook
            }, true)[0];
            // change view
            $scope.booklistView.All = false;
            $scope.booklistView.singleBook = true;
            $scope.booklistView.Purchased = false;
        };

        $scope.readBook = function (bookname) {
            $scope.booklistView.All = false;
            $scope.booklistView.singleBook = false;
            $scope.booklistView.Purchased = false;
            var testBook = "An Everyday Girl—A Project Gutenberg eBook.html";
            $scope.readMode = true;
            $scope.bookReadOn = "book list/" + testBook;

            //get location from db to:
            $scope.readModeFuncs.locationOnPage = 0;
        };

        $scope.readModeFuncs = {
            savePosition: function () {
                $scope.readModeFuncs.locationOnPage = window.scrollY;
                //servlet to db.
            },
            goBack: function () {
                window.scrollTo(0, $scope.readModeFuncs.locationOnPage);
            },
            stopReading: function () {
                $scope.readMode = false;
                $scope.bookReadOn = null;
                return;
            },
            saveAndStop: function () {
                $scope.readModeFuncs.savePosition();
                $scope.readModeFuncs.stopReading();
            }
        };

        // review
        $scope.addReviewShow = [];
        $scope.addReviewShow.push(false); // should handle list per book

        $scope.addReviewOpen = function () {
            $scope.addReviewShow = !$scope.addReviewShow;
        };
        $scope.addReview = function (bookName, reviewText) {
            //var reviewText = $scope.newReview;
            //var bookName = $scope.book.Name;
            var obj = {
                reviewText: reviewText,
                bookName: bookName
            };

            $http.post("ReviewServlet", obj).then(function (response) {
                    // response.data
                    // should response updated reviews list for this book!
                }, function () {

                }

            );
        };


        //like
        $scope.likeBook = function () {
            var obj = {
                bookName: $scope.book.Name
            };
            if (userLikedThisBook($scope.book.likes)) {
                $http.delete("LikeServlet", obj).then(function (response) {
                        // response.data
                        // should response updated likes list for this book!
                    }, function () {

                    }

                );
                var sLike = $scope.singleBook = $filter('filter')($scope.book.likes, {
                    Username: $scope.bookUserData.Username
                }, true)[0];
                var idx = $scope.book.likes.indexOf(sLike);
                $scope.book.likes.splice(idx, 1); // remove this like
            } else {
                $http.post("LikeServlet", obj).then(function (response) {
                        // response.data
                        // should response updated likes list for this book!
                    }, function () {

                    }

                );
            }
        };

        // buy book


        // $scope.userBoughtThisBook = function (bookname){
        //     if ($scope.bookUserData.isLogged === false)
        //         return false;
        //     if (bookname in $scope.purchasedBooklist){ // user bought it
        //         return true;
        //     } else
        //         return false;
        //    // $filter('filter')(myArray, {'id':73})
        //    // if $scope.book.Name
        // };
    }
]);

bookApp.controller('loginController', ['$scope', '$http', 'userData', function ($scope, $http, userData) {

    $scope.loginUserData = userData; //isLogged, loggedUser, adminLogged

    $scope.loginMode = false;
    $scope.registerMode = false;
    $scope.loginFormErrorToggle = false;
    //userData.loggedUser = "";
    $scope.loginUserData.loggedUser = "";
    //$scope.loggedUser = userData.loggedUser;
    //$scope.adminLogged = false;
    //$scope.adminLogged = userData.adminLogged;
    $scope.loginUserData.adminLogged = false;


    $scope.loginUserData.isLogged = isLoggedFunc();

    function isLoggedFunc() {
        $http.get("LoginServlet").then(function (response) {
                $scope.loginUserData.isLogged = true;
                $scope.loginUserData.loggedUser = response.data.username;
                if ("isAdmin" in response.data) {
                    $scope.loginUserData.adminLogged = true;
                }
                $scope.loginUserData.getPurchasedBooksFunc(); // TODO: check
            },
            function (response) {});
    }

    $scope.logoutFunc = function () {
        $http.post("LogoutServlet").then(function () {
            $scope.loginUserData.isLogged = false;
            $scope.loginUserData.loggedUser = "";
            $scope.loginUserData.adminLogged = false;
            $scope.loginUserData.purchasedBooks = [];
            console.log("logout successful");
        }, function () {
            console.log("logout failed");
        });
    };

    $scope.loginFunc = function () {
        if ($scope.loginForm.$invalid) {
            return;
        }
        var obj = {
            Username: $scope.login.User,
            Password: $scope.login.Password
        };

        $http.post("LoginServlet", obj).then(function (response) {
            $scope.loginFormErrorToggle = false;
            // $scope.loginUsernameErrorToggle = false;
            // $scope.loginUsernameError = "";
            // $scope.loginPasswordErrorToggle = false;
            // $scope.loginPasswordError = "";
            $scope.loginUserData.loggedUser = $scope.login.User;
            $scope.loginUserData.isLogged = true;
            if (angular.equals(obj.Username, "admin")) {
                // admin login successful
                $scope.loginUserData.adminLogged = true;
            }
            // upon succsess
            $scope.loginUserData.getPurchasedBooksFunc(); // TODO: check
            $scope.loginMode = false;
        }, function (reposnse) {
            $scope.loginFormErrorToggle = true;
            $scope.loginFormError = "Wrong username or password";
            $scope.loginUserData.loggedUser = "";
            $scope.loginUserData.isLogged = false;
            return;
        });
    };

    $scope.registerFunc = function () {
        console.log("do register");
        if ($scope.registerForm.$invalid) {
            return;
        }
        var obj = {
            Username: $scope.register.User,
            Password: $scope.register.Password,
            Nickname: $scope.register.NickName,
            Email: $scope.register.Email,
            Phone: $scope.register.Telephone.Area + "" + $scope.register.Telephone.Full,
            Address: $scope.register.Address.Street + " " +
                $scope.register.Address.City + " " +
                $scope.register.Address.zip,
            Description: $scope.register.About,
            Photo: $scope.register.photo
        };

        $http.post("RegistrationServlet", obj).then(function (response) {
            $scope.RegisterFormErrorToggle = false;

            $scope.loginUserData.loggedUser = $scope.register.User;
            $scope.loginUserData.isLogged = true;
            $scope.registerMode = false;

            // upon success
            $scope.loginMode = false;
        }, function (reposnse) {
            $scope.RegisterFormErrorToggle = true;
            $scope.registerFormError = "Error filling form";
            $scope.loginUserData.loggedUser = "";
            $scope.loginUserData.isLogged = false;
            return;
        });
        //upon success
    };
    $scope.openLogin = function () {
        $scope.loginMode = !$scope.loginMode;
    };
    $scope.openRegister = function () {
        $scope.registerMode = !$scope.registerMode;
        console.log("register mode active");
    };
}]);

bookApp.controller('adminController', ['$scope', '$http', '$filter', 'userData',
    function ($scope, $http, $filter, userData) {
        $scope.AdminViews = {
            adminMenuView: false,
            usersView: false,
            reviewsView: false,
            transactionsView: false,
            whatToShow: "",
            show: function (whatToShow) {
                $scope.AdminViews.whatToShow = whatToShow;
                switch (whatToShow) {
                    case "reviews":
                        getReviews();
                        break;
                    case "users":
                        getUsers();
                        break;
                    case "transactions":
                        getTransactions();
                        break;
                    case "books":
                        //show regular books
                        break;
                    default:
                        console.log("wrong AdminView");
                }
            }
        };
        // userData; //isLogged, loggedUser, adminLogged
        userData.adminMode = function adminModeFunc() {
            if (userData.adminLogged !== true)
                return;
            $scope.adminMode = true;
            $scope.AdminViews.adminMenuView = true;
            // getTransactions();
            // getUsers();
            // getReviews();
        };

        $scope.adminTransactions = {
            show: false,
            TransActions: []
        };
        $scope.adminUser = {
            show: false,
            users: {},
            deleteUser: function (userToDelete) {
                var obj = {
                    username: userToDelete
                };
                $http.delete("CustomerServlet", obj).then(function (response) {
                    // deleted
                }, function (reposnse) {
                    // failed to delete
                    return;
                });
            }
        };
        $scope.adminReview = {
            show: false,
            Reviews: {},
            approveReview: function (reviewIdtoApprove) {
                var obj = {
                    username: userToDelete
                };
                // $http.delete("CustomersServlet/name/"+userToDelete, obj).then(function (response) {
                $http.delete("customers/name/" + userToDelete, obj).then(function (response) {
                    // deleted
                }, function (reposnse) {
                    // failed to delete
                    return;
                });
            }
        };

        function getTransactions() {
            $http.get("TransactionsServlet").then(function (response) {
                $scope.adminTransactions.TransActions = response.data;
            }, function (reposnse) {
                // failed getting transactions
                return;
            });
        }

        function getUsers() {
            $http.get("customers").then(function (response) {
                $scope.adminUser.users = response.data;
            }, function (reposnse) {
                // failed gettin transactions
                return;
            });
        }

        function getReviews() {
            $http.get("ReviewsServlet").then(function (response) {
                $scope.adminReview.Reviews = response.data;
            }, function (reposnse) {
                // failed gettin transactions
                return;
            });
        }

        //admin controller
    }
]);


// jQuery stuff

//support for tooltips (from w3schools)
// $(document).ready(function(){
//     $('[data-toggle="tooltip"]').tooltip();
// });