console.log("This is script file name")

const toggleSidebar = () => {

  if ($(".sidebar").is(":visible")) {
    //true or band krna hai
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
  }
  else {
    //false or show krna hai
    $(".sidebar").css("display", "block");
    $(".content").css("margin-left", "20%");
  }
};

