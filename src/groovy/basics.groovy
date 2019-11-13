package groovy

import groovy.transform.Immutable

//grab follows maven group:module:version syntax
@Grab('commons-lang:commons-lang:2.4')
import org.apache.commons.lang.ClassUtils


//you can use a shebang for a script
// #!/usr/bin/env grooy


//leave off parenthesis if you wish
def val = URLEncoder.encode 'a b', 'UTF-8'
println val


//auto imports
//groovy.lang (tuple support etc etc )
//groovy.util
//java.lang
//java.util
//java.net
//java.io
//java.Math.BigInteger and BigDecimal
def tuple = new Tuple('one', 1, new Expando(number: 1))
println tuple.size()
println tuple.get(0)

def noReturnStatement() {
	15
}

println noReturnStatement()

// == means equality not identity, unlike java
assert(1 == 1)
assert 1 == 1

//dynamic types
def x = 3
assert x == 3
println x.class

//uncomment to see what happens when assertion fails
//assert x == 5

//use assertions instead of comments as they never go out of date


class Book {
	private String title
	
	Book(String theTitle) {
		title = theTitle
	}
	
	String getTitle() {
		return title
	}
	
}

//note, no type or def required for parameter
String getTitleBackwards(book) {
	String title = book.getTitle()
	title.reverse()
}


Book fina = new Book('Groovy in action')
assert fina.getTitle() == 'Groovy in action'
assert getTitleBackwards(fina) == 'noitca ni yvoorG'


//properties
class BookBean {
	String title
}

def groovyBook = new BookBean()
groovyBook.setTitle('Groovy in action')

//an automatic property notation, property dot notation calls getter and setter, not field
groovyBook.title = 'Groovy in action'


@Immutable class FixedBook {
	String title
}

def gina = new FixedBook('Groovy')
def regina = new FixedBook(title: 'Groovy')
//can compare on value
assert gina == regina

try {
	gina.title = 'Fred'
} catch (ReadOnlyPropertyException ex) {
	println "Expected error $ex.message"
}


//using the grab for commons utils ClassUtils
class Outer {
	class Inner {}
}

assert !ClassUtils.isInnerClass(Outer)
assert ClassUtils.isInnerClass(Outer.Inner)

//GStrings
def nick = 'ReGina'
def book = 'Groovy in Action'
assert "$nick is $book" == 'ReGina is Groovy in Action'

//language level regular expressions
// /reg/ is slashy syntax =~ is the find operator
assert '12345' =~ /\d+/
println '123ROO45'.replaceAll(/\d/, 'x')

//everything is an object
//numbers are objects
def xnu = 1
def ynu = 2

assert xnu + ynu == 3
assert xnu.plus(ynu) == 3
assert x instanceof Integer

//lists
//makes using lists like using arrays, don't need list api stuff
def roman = ['', 'I', 'II', 'III', 'IV', 'VI', 'VII']
assert roman[4] == 'IV'

//can go outside bounds and list will resize
roman[8] = 'VIII'
assert roman.size() == 9

//maps

def httpx = [ 100: 'CONTINUE', 200: 'OK', 400: 'BAD REQUEST' ]
httpx[410] = 'UNKNOWN'
println httpx[410]

//note the asImmutable
def http = [ 100: 'CONTINUE', 200: 'OK', 400: 'BAD REQUEST' ].asImmutable()
assert http[200] == 'OK'

try {
	http[410] = 'UNKNOWN'
} catch (final UnsupportedOperationException ex) {
	println "Expected Error $ex"
}


//ranges 
def aRange = 1..10
assert aRange.contains(5)
assert !aRange.contains(15)
assert aRange.size() == 10
assert aRange.from == 1
assert aRange.to == 10
assert aRange.reverse() == 10..1


//closures
[1,2,3].each {entry -> println entry}

def totalClinks = 0
def partyPeople = 100

//upto does something for the number up to a ceiling
//takes a clojure, passes in the current number
1.upto(partyPeople) { guestNumber ->
	clinksWithGuest = guestNumber - 1
	totalClinks += clinksWithGuest
}

assert totalClinks == (partyPeople * (partyPeople - 1)) / 2

def gauss(number) {
	(number * (number - 1) / 2)
}

println gauss (101)



//control structures
if (false) assert false
if (null) {
	assert false
} else {
	assert true
}

//for in
def myXCounter = 0
for (myX in 0..9) {
	myXCounter++
}


def myList = [0, 1, 2]
for (j in myList) {
	assert j == myList[j]
}

//clojure
myList.each() { item ->
	assert item == myList[item]
}

















