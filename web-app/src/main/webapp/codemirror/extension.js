/**
 * Created by velsubra on 3/12/15.
 */

if (server) {
    TamilFactory.context += "/rest";
}


var EDIT_TRANSLIT = TamilFactory.createTransliterator();
var EDIT_PARSE = TamilFactory.createParser(1);
var EDIT_DICTIONARY = TamilFactory.createDictionary(50);



//Starts with pulli and then aa
var glyph = ['\u0BCD', '\u0BBE', '\u0BBF', '\u0BC0', '\u0BC1', '\u0BC2', '\u0BC6', '\u0BC7', '\u0BC8', '\u0BCA', '\u0BCB', '\u0BCC'];
var TRANSLIT = true;
function containsInArray(a, obj) {
    for (var i = 0; i < a.length; i++) {

        if (a[i] === obj) {

            return true;
        }
    }
    return false;
}


function get_glyph_count(text, max) {
    if (!text || max <= 0) return 0;
    var c = 0;
    for (var i = 0; i < max; i++) {
        if (containsInArray(glyph, text.charAt(i))) {
            c++;
        }
    }
    return c;
}

function get_last_word(text, max) {
    if (!text || max <= 0) return "";
    var word = "";
    for (var i = max - 1; i >= 0; i--) {
        var space = text.charAt(i);
        if (space == ' ') {
            return word;
        }
        word = space + word;
    }
    return word;
}


function get_current_word(cm) {
    var pos = cm.getCursor();
    var currentToken = cm.getTokenAt(pos, true);
    if (!currentToken || !currentToken.string) {
        return "";
    }
    return get_last_word(currentToken.string, pos.ch);
}


function handleCursorForTamilGlyphs(cm, left, shiftKey) {
    var pos = cm.getCursor();


    var currentToken = cm.getTokenAt(pos, true);
    if (!currentToken || !currentToken.string) {
        return;
    }


    if (!shiftKey && currentToken.string.length > pos.ch) {
        var currentChar = currentToken.string.charAt(pos.ch);
        //  console.log(currentChar);

        if (containsInArray(glyph, currentChar)) {
            if (left && pos.ch > 0) {

                pos.ch--;
            } else {
                pos.ch++;
            }
            //  console.log("setting:" + pos + ":" + shiftKey);
            cm.setCursor(pos);
            return;
        }
    }


    var current_word = get_current_word(cm);

    if (TRANSLIT) {

        _translitCallback.fullword = current_word;
        _translitCallback.widget = {
            line: pos.line,
            ch: (current_word ? pos.ch - current_word.length : pos.ch)
        };

        EDIT_TRANSLIT.transliterateAsync(_translitCallback, current_word, "110,115");


    } else {
        var current_tamil_word = "(" + (pos.line + 1) + "," + (pos.ch + 1 - get_glyph_count(currentToken.string, pos.ch)) + ")";
        if (tool_tip.parentNode) {
            tool_tip.parentNode.removeChild(tool_tip);
        }
        cm.addWidget(pos, tool_tip, true);
        $('#tool_tip').html(current_tamil_word);

    }
    if ($('.CodeMirror-hints') && $('.CodeMirror-hints').is(":visible")) {
        $('#tool_tip').hide();
    } else {

        $('#tool_tip').show();

    }
    // console.log("showing ...")  ;


}


var myCodeMirror = null;
var tool_tip = null;
$(document).ready(function () {


    myCodeMirror = CodeMirror.fromTextArea(document.getElementById("edit"), {
            styleActiveLine: true,
            extraKeys: {"Ctrl-Space": _autocomplete},
            lineNumbers: true,
            searchMode: 'inline',
            lineWrapping: true
//                        lineNumberFormatter : function(line) {
//                           // var cm = $('.CodeMirror')[0].CodeMirror;
//                            //var cur = cm.getCursor();
//                            return line + ":" + cur.ch;
//                        }
        }
    );

    myCodeMirror.on("change", _onChange);
    myCodeMirror.on("keyHandled", _onKeyHandled);
    myCodeMirror.on("cursorActivity", _cursorActivity);
    myCodeMirror.on("beforeChange", _beforeChange);
    myCodeMirror.on("keydown", _keydown);
    myCodeMirror.on("contextmenu", _contextmenu);
    myCodeMirror.on("dblclick", _contextmenu);

    $('<div/>', {
        id: 'tool_tip',
        class: 'temp_tamil_tip'

    }).appendTo('body');

    tool_tip = document.getElementById('tool_tip');

    setTimeout(_backgroundSpellCheck, 100);
});


function _processWordForSpellCheck(lineno, startCol, fullword) {
    var instance_callback = new _suggestionCallback(lineno, startCol, fullword);

    var transresult = EDIT_TRANSLIT.transliterate(fullword, "110,115");
    EDIT_PARSE.parseAsync(instance_callback.method_callback, transresult.tamil, "");


}


function _processLineForSpellCheck(lineno, fullLine) {
    if (fullLine) {

        var token = "";
        for (var i = 0; i < fullLine.length; i++) {
            var ch = fullLine.charAt(i);
            if (ch == ' ') {
                if (token.trim()) {
                    _processWordForSpellCheck(lineno, i - token.trim().length, token.trim());

                }

                token = "";
            } else {
                token += ch;
            }
        }

        //last token
        token = token.trim();
        if (token) {
            _processWordForSpellCheck(lineno, fullLine.length - token.length, token);
        }


    }
}

function _translitCallback(result) {
    //console.log("Translit Response received:");
    //console.log(result);

    if (_translitCallback.fullword === result.given) {

        if (result.tamil) {
            if (tool_tip.parentNode) {
                tool_tip.parentNode.removeChild(tool_tip);
            }
            myCodeMirror.addWidget(_translitCallback.widget, tool_tip, true);
            $('#tool_tip').html(result.tamil);

        } else {

            $('#tool_tip').html("");
        }

    } else {

        $('#tool_tip').html("");
    }
}


function _suggestionCallback(lineno, startCol, fullword) {


    this.method_callback = function (current_tamil_obj) {
        // console.log(current_tamil_obj);
        if (!current_tamil_obj.parsed) {

            // console.log(current_tamil_obj);
            myCodeMirror.markText({
                    line: lineno,
                    ch: startCol
                }, {
                    line: lineno,
                    ch: startCol + fullword.length
                }, {
                    inclusiveLeft: true,
                    inclusiveRight: false,
                    className: "note"
                }
            )
        }
    }
    return this;
}

function _backgroundSpellCheck() {
    var markers_old = [];
    var marks = myCodeMirror.getAllMarks();
    if (marks) {
        for (var i = 0; i < marks.length; i++) {
            markers_old.push(marks[i]);
        }
    }

    myCodeMirror.eachLine(function (lineHandle) {
        var text = lineHandle.text;
        var lineno = lineHandle.lineNo();
        _processLineForSpellCheck(lineno, text);

    });


    for (var i = 0; i < markers_old.length; i++) {
        markers_old[i].clear();
    }

//            if (myCodeMirror.getAllMarks().length > 0) {
//                //setTimeout(_backgroundSpellCheck, 5000);
//            }    else {
//              //  setTimeout(_backgroundSpellCheck, 10000);
//            }
    // console.log("All current marks:" + myCodeMirror.getAllMarks().length);
}

function _beforeChange(cm, change) {
    if (!TRANSLIT) return;
    var space = change.text.length == 1 && change.text[0] == " ";
    var newline = change.text.length == 2 && change.text[0] == "" && change.text[1] == "";
    if (space || newline) {
        var pos = cm.getCursor();
        var current_word = get_current_word(cm);
        if (current_word) {
            var current_tamil_word = EDIT_TRANSLIT.transliterate(current_word, "110,115").tamil;
            if (current_word != current_tamil_word) {
                change.cancel();
                // console.log("replaced ....");
                cm.replaceRange(current_tamil_word + (space ? " " : "\n"), {
                        line: pos.line,
                        ch: pos.ch - current_word.length
                    }, pos, "tamil"
                );
            }
        }
    }
}

function _cursorActivity(cm) {

    // console.log("Cursor----------:" + cm.getCursor() + ":" + window.event);
    handleCursorForTamilGlyphs(cm, false, window.event ? window.event.shiftKey : false);
}

function _keydown(cm, event) {
    if (event.ctrlKey && event.which == 84) {
        TRANSLIT = !TRANSLIT;
        handleCursorForTamilGlyphs(cm, false, event ? event.shiftKey : window.event ? window.event.shiftKey : false);
        event.preventDefault();
        return;
    }
}

function _contextmenu(cm, event) {
    _autocomplete(cm);
}


function _onKeyHandled(cm, name, event) {
    // console.log(event);

    //ctrl + space is handled.
    if (event.ctrlKey && event.which == 32) {
        return;
    }
    //backspace.
    //if (event.keyCode == 8) {
    //    _autocomplete(cm);
    //}

    handleCursorForTamilGlyphs(cm, event.keyCode == 37 || event.keyCode == 8, event.shiftKey);
    var pos = cm.getCursor();

    if (event.keyCode == 13) {
        var currentToken = cm.getTokenAt(pos, true);
        if (!currentToken) {
            return;
        }
    }
}


function _provideHint(cm, callback, options) {

    // console.log(callback);

    var current_word = get_current_word(cm);

     var trans = EDIT_TRANSLIT.transliterate(current_word, "110,115").tamil;
    // if (current_word) {
    EDIT_DICTIONARY.searchAsync(function (suggestedwords) {
        // console.log(suggestedwords);
        var current_word_now = get_current_word(cm);
        var trans_now = EDIT_TRANSLIT.transliterate(current_word_now, "110,115").tamil;

        if (trans_now != suggestedwords.given) {
            _autocomplete(cm);
            // return;
        }
        var data = [];
        if (suggestedwords && suggestedwords.list) {
            for (var sug = 0; sug < suggestedwords.list.length; sug++) {

                if (sug == 1 && current_word) {
                    // getTamilWord(current_word, false, false, true).tamilWord;
                    if (trans_now != suggestedwords[0]) {
                        data.push({
                            text: trans_now,
                            displayText: ">> " + trans_now + "(Space)"
                        });
                    }
                }
                data.push({
                    text: suggestedwords.list[sug].tamil,
                    displayText: suggestedwords.list[sug].tamil + "     " + suggestedwords.list[sug].type + (sug == 0 ? "(Enter)" : "")
                });
            }
        }
        var pos = cm.getCursor();
        var from_word = {line: pos.line, ch: pos.ch - current_word.length};


        callback({
            list: data, from: from_word, to: cm.getCursor()
        });


    }, trans, "165");


    //   }

}

_provideHint.async = true;

function _autocomplete(cm) {
    EDIT_TRANSLIT.transliterateAsync(_translitCallback, get_current_word(cm), "110,115");

    if ($('.CodeMirror-hints') && $('.CodeMirror-hints').is(":visible")) {

    }
    else {
        cm.showHint({
            hint: _provideHint

        });
    }


}


var SPELL_CHECK = null;
function _onChange(cm, changed) {
    // no change
    if (SPELL_CHECK) {
        clearTimeout(SPELL_CHECK);
    }
    SPELL_CHECK = setTimeout(_backgroundSpellCheck, 200);

}