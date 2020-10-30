
function InitVocabularyInfoPage(WordJson, SentenceJson) {
	var now_vocabulary_json = null;  //现在浏览的单词的json数据
    var now_sentence_json = null; //当前句子的json
    var now_sentence_index = -1; //当前句子在json数组中的下标
    var exampleSentencesJson  = null; //返回的json
    var sentencesNum = 0;

    var coverForInfo = document.getElementById('CoverForInfo');
    var vocabularyInfoPage = document.getElementsByClassName('vocabularyInfo')[0];
    var btnCloseInfo = document.getElementsByClassName('closeInfo')[0];
    var vocabularySpell = document.getElementsByClassName('vocabularySpell')[0];
    var vocabularySoundMark = document.getElementsByClassName('soundMark')[0];
    var vocabularySound = document.getElementsByClassName('vocabulary')[0].getElementsByClassName('btnSound')[0];
    var vocabularyTranslation = document.getElementsByClassName('vocabularyTranslation')[0];

    var sentenceInfo = document.getElementsByClassName('sentenceInfo')[0];
    var sentenceSpell = sentenceInfo.getElementsByClassName('sentenceSpell')[0];
    var sentenceTranslation = sentenceInfo.getElementsByClassName('sentenceTranslation')[0];
    var sentenceBtnSound = sentenceInfo.getElementsByClassName('btnSound')[0];
    var btnNextSentence = document.getElementById('btnNext');
    var btnPreSentence = document.getElementById('btnPre');

    var addVocabulary = document.getElementById('btnAddV');
    var modVocabulary = document.getElementById('btnModV');
    var delVocabulary = document.getElementById('btnDelV');
    var addSentence = document.getElementById('btnAddS');
    var modSentence = document.getElementById('btnModS');
    var delSentence = document.getElementById('btnDelS');

    btnCloseInfo.onclick = function () {
        coverForInfo.style.display = 'none';
        vocabularyInfoPage.style.display = 'none';

        now_sentence_json = null;
        now_vocabulary_json = null;
        now_sentence_index = -1;
        exampleSentencesJson  = null;
        sentencesNum = 0;
    };

    btnNextSentence.onclick = function () {
        now_sentence_index += 1;
        showExampleSentence();
    };
    btnPreSentence.onclick = function () {
        now_sentence_index -= 1;
        showExampleSentence();
    };

    function showVocabularyInfoPage(word_Json, sentence_Json) {
        coverForInfo.style.display = 'block';
        vocabularyInfoPage.style.display = 'block';
        showVocabularyInfo_Word(word_Json);
        if(!(sentence_Json instanceof Array)){  //是否为数组判断
            //后面的许多操作是基于json数组来的，如果只有一个json对象，就会出现很多错
            var arrayJsonStr = "[" + JSON.stringify(sentence_Json) + "]"; //转为字符串形式的json数组
            var arrayJson = JSON.parse(arrayJsonStr); //转为json数组
            showVocabularyInfo_Sentences(arrayJson);
        }
        else
            showVocabularyInfo_Sentences(sentence_Json);

    }

    function showVocabularyInfo_Word(json) {
        // {id, name, translation, soundMark, soundFilePath}
        now_vocabulary_json = json;
        vocabularySpell.innerHTML = json.name;
        vocabularyTranslation.innerHTML = json.translation || '暂无翻译';
        vocabularySoundMark.innerHTML = json.soundmark || '暂无音标';
        vocabularySound.style.display = 'block';
        vocabularySound.src = json.soundfilepath; //音频文件保存路径，相对的
        if(ISNULL(json.soundfilepath)) //是否有音频
            vocabularySound.style.display = 'none';
    }
    
   

    function showVocabularyInfo_Sentences(json) {
        exampleSentencesJson = json;
        now_sentence_index = 0;
        showExampleSentence();
    }

    //展示挡墙例句信息
    function showExampleSentence() {
        // {id, name, translation, soundFilePath}
        sentencesNum = exampleSentencesJson.length;
        
        btnPreSentence.style.display = 'none';
        btnNextSentence.style.display = 'none';
        
        if(sentencesNum === 0){  //都没了
            modSentence.style.display = 'none';
            delSentence.style.display = 'none';
            sentenceBtnSound.style.display = 'none';
            sentenceSpell.innerHTML = "暂无例句，可尝试添加";
            sentenceTranslation.innerHTML = '';
            return false;
        }
        else if(now_sentence_index === sentencesNum && sentencesNum > 0){ //删除最后一个例句，显示前一句
            now_sentence_index--;
            showExampleSentence();
            return false;
        }
        else{  //填充数据	
            modSentence.style.display = 'block';
            delSentence.style.display = 'block';
            sentenceBtnSound.style.display = 'block';
            now_sentence_json = exampleSentencesJson[now_sentence_index];//当前例句json
            sentenceSpell.innerHTML = now_sentence_json.name;           
            sentenceTranslation.innerHTML =  exampleSentencesJson[now_sentence_index].translation || '暂无翻译';
            sentenceBtnSound.src = exampleSentencesJson[now_sentence_index].soundfilepath;  //音频文件保存路径，相对的
            if(ISNULL(sentenceBtnSound.src)){
                sentenceBtnSound.style.display = 'none';
            }
            
          
            //对例句切换按钮的显示判断
            if(now_sentence_index === 0)
                btnPreSentence.style.display = 'none';
            else
                btnPreSentence.style.display = 'block';
            if(now_sentence_index === sentencesNum-1)
                btnNextSentence.style.display = 'none';
            else
                btnNextSentence.style.display = 'block';
        }
    }

    
    
    var audio = null;  //单词和例句播放共享，反正同时只能一个播放
    //播放音频
    sentenceBtnSound.onclick = vocabularySound.onclick = function(){
    	if(audio != null)  //上一个暂停
    		audio.pause();
    	audio = new Audio(this.src);  //创建新音频
    	audio.play(); //播放
    };
    
  //词汇/例句的增删查改
    delVocabulary.onclick = function () {
        if(confirm("如果删除此单词，所有例句都将清空，是否继续")){
            //传递后台
            jQuery.get(vocabularyInfoChangeServlet, {op:"delVocabulary", id:now_vocabulary_json.id}, rs=>{
            	var json = JSON.parse(rs);
            	if(json.code == 500){
            		//删除成功
            		alert('删除成功');
                    btnCloseInfo.onclick(); //关闭页面
                    document.getElementById('txtSearch').value = '';
                    document.getElementById('txtSearch').onkeyup(undefined);
            	}else{
            		alert('删除失败');
            	}
            });
        }
    };

    delSentence.onclick = function () {
        exampleSentencesJson.splice(now_sentence_index, 1);
        jQuery.get(vocabularyInfoChangeServlet,{op:"delSentence", id:now_sentence_json.id}, rs=>{
        	var json = JSON.parse(rs);
        	if(json.code == 500){
        		;  //啥也不用做
        	}else{
        		alert('删除失败');
        	}
        });
        showExampleSentence();
    };

    addSentence.onclick = function(){
        ShowOPForm("addSentence", undefined, "为" + now_vocabulary_json.name + "新增例句", now_vocabulary_json.id);
    };

    addVocabulary.onclick = function(){
        ShowOPForm("addVocabulary", undefined, "新增单词");
    };

    modSentence.onclick = function(){
        ShowOPForm("modSentence", now_sentence_json,"为" + now_vocabulary_json.name + "修改例句");
    };

    modVocabulary.onclick = function(){
        ShowOPForm("modVocabulary", now_vocabulary_json, "修改单词");
    }; 
	
	
    showVocabularyInfoPage(WordJson, SentenceJson);
}

function ISNULL(obj){
	if(obj == undefined || obj == null || obj == '')
		return true;
	else
		return false;
}



