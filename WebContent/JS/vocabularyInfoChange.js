 function ShowOPForm(op, json, opTitle, vocabularyID) {
        var CoverForForm = document.getElementById('CoverForForm');
        var OPForm = document.getElementById('OPForm');
        var OPBtnCancel = document.getElementById('OPBtnCancel');
        var FormTitle = document.getElementById('FormTitle');
        var txtNewWord = document.getElementById('txtNewWord');
        var txtNewTranslation = document.getElementById('txtNewTranslation');
        var OPBtnSubmit = document.getElementById('OPBtnSubmit');
        var txtNewSoundMark = document.getElementById('txtNewSoundMark');
        var soundMarkArea = document.getElementsByClassName('input5')[0];
        var file = document.getElementById('uploadFile');
        var fileOuter = file.outerHTML;
        var isEmpty = false;
        var isVocabularyOP = false;
        var isNew = false;
        var fileOuterHtml = document.getElementsByClassName('input3')[0];
        var outerStr = '<label>音频文件</label><input type="file" name="uploadFile" id="uploadFile" accept="audio/mpeg,audio/ogg"/>';
        

        OPBtnCancel.onclick = function(){
        	 CoverForForm.style.display = 'none';
             OPForm.style.display = 'none';
        };
       

        
        CoverForForm.style.display = 'block';
        OPForm.style.display = 'block';

        FormTitle.innerHTML = opTitle;
       
        soundMarkArea.style.display = 'none';
        
        
        
        isNew = false;
        isVocabularyOP = false;
        if(op == 'addVocabulary' || op == 'modVocabulary'){
            isVocabularyOP = true;
            soundMarkArea.style.display = 'block';
        }

        
        if(op == 'addVocabulary' || op == 'addSentence'){
        	isNew = true;  //增加新东西就不需要补全输入框数据了
        }
        	
        if(isNew || json == undefined){  //清空
        	txtNewTranslation.value = '';
        	txtNewWord.value = '';
        	txtNewSoundMark.value = '';
        }else{
           	 txtNewWord.value = json.name || '';
             txtNewTranslation.value = json.translation || ''; 
             if(isVocabularyOP)
            	 txtNewSoundMark.value = json.soundmark.toString() || '';
        }
        
        
        
        OPBtnSubmit.onclick = function () {
            isEmpty = false;
            if(ISNULL(txtNewTranslation.value.trim()) || ISNULL(txtNewWord.value.trim())){
                isEmpty = true;
            }
            
            if(isVocabularyOP){
                if(ISNULL(txtNewSoundMark.value.trim())) {
                    isEmpty = true;
                }
            }

            if(isEmpty){
                alert('除音频文件可以暂时不上传外，其他不能为空');
            }else{
            	  var word = txtNewWord.value.trim();
                  var tans = txtNewTranslation.value.trim();
                  var mark = txtNewSoundMark.value.trim();     
               		
                  
                  if(op == 'addVocabulary'){  
                	/*
                	增加词汇
                	*/
                	  jQuery.ajaxFileUpload({ 
                          url: "VocabularyInfoChangeServlet?op=addVocabulary",
                          secureuri: false,
                          fileElementId: "uploadFile",
                          dataType: "text",    //在网上一搜发现，有时候后台连接成功了，但去执行了error函数，主要与dataType有关
                          data: {name:word, translation:tans, soundMark:mark},
                          success:function (data,status) {
                              var json = JSON.parse(data);
                              if(json.code == 600){
                            	  alert('新增成功');
                              }else if(json.code == 606){
                            	  alert('除了音频文件，数据正常添加');
                              }else{
                            	  alert('新增失败');
                              }
                             
                              OPBtnCancel.onclick(); //触发取消按钮
                              fileOuterHtml.innerHTML = outerStr;
                          },
                          error:function (data,status,e) {
                              alert('服务器连接失败');
                          }
                      });
                  }else if(op == 'addSentence'){
                	  /*
                  	增加例句
                  	*/
                	  jQuery.ajaxFileUpload({
                          url: "VocabularyInfoChangeServlet?op=addSentence",
                          secureuri: false,
                          fileElementId: "uploadFile",
                          dataType: "text",
                          data: {name:word, translation:tans,vocabulary_id:vocabularyID},
                          success:function (data,status) {
                              var json = JSON.parse(data);
                              if(json.code == 600){
                            	  alert('新增成功');
                              }else if(json.code == 606){
                            	  alert('除了音频文件，数据正常添加');
                              }else{
                            	  alert('新增失败');
                              }
                              
                              OPBtnCancel.onclick(); //触发取消按钮
                              fileOuterHtml.innerHTML = outerStr;
                              document.getElementById('btnSearch').onclick();  //重新执行搜索，相当于刷新吧,反正看不出来
                          },
                          error:function (data,status,e) {
                              alert('服务器连接失败');
                          }
                      });
                  }else if(op == 'modVocabulary'){
                	  /*
                  	修改词汇
                  	*/
                	  jQuery.ajaxFileUpload({
                          url: "VocabularyInfoChangeServlet?op=modVocabulary",
                          secureuri: false,
                          fileElementId: "uploadFile",
                          dataType: "text",
                          data: {name:word, translation:tans,soundMark:mark, vocabulary_id:json.id},
                          success:function (data,status) {
                              var json = JSON.parse(data);
                              if(json.code == 600){
                            	  alert('修改成功');
                              }else if(json.code == 606){
                            	  alert('除了音频文件，数据正常修改');
                              }else{
                            	  alert('修改失败');
                              }
                              OPBtnCancel.onclick();  
                              fileOuterHtml.innerHTML = outerStr;
                              document.getElementById('txtSearch').value = word; //把搜索框单词改成这个，然后在刷新
                              document.getElementById('txtSearch').onkeyup(undefined); //刷新搜索结果
                              document.getElementById('btnSearch').onclick();  //重新执行搜索，相当于刷新吧,反正看不出来
                          },
                          error:function (data,status,e) {
                              alert('服务器连接失败');
                          }
                      });
                  }else if(op == 'modSentence'){
                	  /*
                  	修改例句
                  	*/
                	  jQuery.ajaxFileUpload({
                          url: "VocabularyInfoChangeServlet?op=modSentence",
                          secureuri: false,
                          fileElementId: "uploadFile",
                          dataType: "text",
                          data: {name:word, translation:tans,sentence_id:json.id},
                          success:function (data,status) {
                              var json = JSON.parse(data);
                              if(json.code == 600){
                            	  alert('修改成功');
                              }else if(json.code == 606){
                            	  alert('除了音频文件，数据正常修改');
                              }else{
                            	  alert('修改失败');
                              }
                              OPBtnCancel.onclick(); //触发取消按钮
                              fileOuterHtml.innerHTML = outerStr;
                              document.getElementById('btnSearch').onclick();  
                          },
                          error:function (data,status,e) {
                              alert('服务器连接失败');
                          }
                      });
                  } 
            }
            
            return false;
        }
    }
