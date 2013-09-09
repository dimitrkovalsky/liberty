package liberty.helpers

import liberty.entities.RecognitionResult
import liberty.entities.Grammar


object GrammarRegistry {
    var grammars: Map[Int, Grammar] = Map()
    var names: List[Grammar] = Nil

    def addGrammar(grammar: Grammar) {
        grammars += grammar.label -> grammar
    }

    def getGrammar(recognized: RecognitionResult): Grammar = grammars(recognized.label)

    def addName(name: Grammar) {
        names = names ::: List(name)
    }

    def addName(list: List[Grammar]) {
        list.foreach(addName)
    }

    def isName(recognized: RecognitionResult): Boolean = {
        names.foreach(x => if (x.label == recognized.label) return true)
        false
    }
}
