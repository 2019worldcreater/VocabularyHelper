
 $(function () {
		 var isFindWord = false;  //下拉框中有且有一个单词才叫找到
		 var oldInputWord = ''; //keyup执行前的文本框的值
	        $('#txtSearch').onkeyup = function () {
	        	var notFoundLog = document.getElementById('404Moon');  //月亮404
	        	notFoundLog.style.display = 'none';
				Move(notFoundLog, 'opacity', 0);
				
	        	var word = this.value.trim().toLowerCase();
	        	if(word == null || word == '' || word == undefined) { //非空
	        			ShowResults(undefined, false);
	        			oldInputWord = '';
	        	}
	        	else{ //
		            //发送文本值给后端
		            if(word != oldInputWord){  //中文输入法下，这个keyup事件会执行两次！！！
		            	oldInputWord = word;
		            	jQuery.get(vocabularyServletUrl, {op:"findRelativeWords", value:word}, result =>{
		            			var json = JSON.parse(result);
		            		
								if(json.code == 404 || json.code == 100)
									ShowResults(undefined, false);
								else
				                	ShowResults(json, true);
			            });	
		            }
	        	}
	        };
	        
	        
	        //点击搜索查看词汇详细信息
	         document.getElementById('btnSearch').onclick =  function () {
	         	var notFoundLog = document.getElementById('404Moon');  //月亮404
               if(isFindWord){  //是否找到了唯一的单词
               	notFoundLog.style.display = 'none';
					Move(notFoundLog, 'opacity', 0);

                   var word = document.getElementById('txtSearch').value.trim().toLowerCase();

                   jQuery.get(vocabularyServletUrl, {op:"findVocabularyInfo", value:word}, rs=>{
                        var word_json = JSON.parse(rs); //取得单词信息
                   
                        //取得例句json数据
                        if(parseInt(word_json.id) > 0){
                       	 jQuery.get(vocabularyServletUrl, {op:"findVocabularySentences", id:word_json.id}, rs=>{
                           	 var sentences_json = JSON.parse(rs);
                     
                           	//显示单词详细信息页面
                                InitVocabularyInfoPage(word_json, sentences_json); 
                            });
                        }               
                       /*
                       	之前本来是写在这的，后面发现请求后台是由时间延迟的，这里的sentences_json还是null
                       	实际上先执行了下面这个代码在获取sentences_json的
                       	InitVocabularyInfoPage(word_json, sentences_json);
                       */
                   });
               }else{
					notFoundLog.style.display = 'block';
					Move(notFoundLog, 'opacity', 100);
				}
           };
	        
	      //将搜索结果填写在下拉框中
	        function ShowResults(json, isEmpty){
	            var searchResult = document.getElementsByClassName('searchResult')[0];
	            var resultLi = searchResult.getElementsByTagName('li');
	           
	            if(!isEmpty){
	                searchResult.style.display = 'none';	                
	                isFindWord = false;
	                return false;
	            }
	             
	            if(searchResult.style.display == 'none')
	            	searchResult.style.height = '0';  //以免一下子跳出来
	            
	            for(var i = 0; i < resultLi.length; i++){
	                resultLi[i].innerHTML = '';
	            }
	            
	            searchResult.style.display = 'block';
	    		var len = json.length;
	    		
	            for(var i = 0; i < len; i++){
	                resultLi[i].innerHTML = json[i].name;
	            }
	            
	            if(len == 1 && resultLi[0].innerHTML == document.getElementById('txtSearch').value.trim().toLowerCase()){
	            	isFindWord = true;
	            }else{
	            	isFindWord = false;
	            }
	            Move(searchResult, 'height', len*resultLi[0].offsetHeight);
	      }
	    });



    //对部分展示效果的处理
    $(function () {
        var txtSearch = document.getElementById('txtSearch');
        var searchResult = document.getElementsByClassName('searchResult')[0];
        var resultLi = searchResult.getElementsByTagName('li');
        for(var i = 0; i < resultLi.length; i++) {
            //实现移入补全单词框,变背景色
            resultLi[i].onmouseover = function () {
                this.style.backgroundColor = 'rgba(157,183,255,0.86)';
            };
            resultLi[i].onmouseout = function () {
                this.style.backgroundColor = 'white';
            };
            //点击搜索结果，搜索框文本变为点击的搜索结果
            resultLi[i].onclick = function () {
                txtSearch.value = this.innerHTML;
                txtSearch.onkeyup(undefined);
            };
        }

        document.getElementById('btnSearch').onmouseover = document.getElementById('btnShowLogin').onmouseover = function () {
            Move(this, 'opacity', 100);
        };
        document.getElementById('btnSearch').onmouseout = document.getElementById('btnShowLogin').onmouseout = function () {
            Move(this, 'opacity', 70);
        };
    });