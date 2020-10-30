/**
 * 
 */

if(location.href.indexOf('?') < 0)
        location.href = location.href + "?time=" + new Date().getTime();
    
var vocabularyServletUrl = "VocabularyServlet";
var uploadFilePath = "";
var vocabularyInfoChangeServlet = "VocabularyInfoChangeServlet";