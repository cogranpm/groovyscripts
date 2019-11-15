package groovy

import java.awt.*

import org.codehaus.groovy.runtime.typehandling.GroovyCastException

import groovy.transform.Immutable

import static java.util.Calendar.*

//no primitive types
long mylong = 100L
float myfloat = 100.0F
double mydouble = 1.23d
BigInteger myBI = 123g
BigDecimal myBD = 1.23g

Double dbg = 1.23d
Float flt = 1.23f
Long lng = 123l //lowercase l is discouraged, looks like a 1
Integer inte = 45

//BigDecimal is the default for a floating point number

//groovy autoboxes when calling into java
assert 'ABCDE'.indexOf(67) == 2

//groovy operators will run as method calls behind the scenes
assert 1 + 1 == 1.plus(1)

//groovy dynamic types are actually Object
//however it uses type inference where possible
def a = 1
println a.class
def bogStandard = "bog"
println bogStandard.class

//fully typesafe at runtime


//can cast a list or map to an arbitrary class
Point topLeft = new Point(0, 0) //classic
Point botRight = [100, 100] 	//list cast
Point center = [x:50, y:50] 	//map cast

def rect = new Rectangle()
rect.location = [0, 0]
rect.size = [width: 100, height: 100] //very readable code here

assert botRight instanceof Point
assert center instanceof Point

try {
	Point nogood = [100, 120, 5]
} catch (GroovyCastException ex) {
	println "Expected Error: $ex.message"
}

//duck typing
//don't need to specify type if passing around

class Duck {
	def quack() {
		return "quack"
	}
}


def findIt() {
	new Duck()
}

def logIt(node) {
	println "logIt called ${node.quack()}"
}

def storeIt(node) {
	println "storeIt called ${node.quack()}"
}

//need not care what the object type is, just passing it around to api's
def node = findIt()
logIt node
storeIt node


//overriding operators
/*
 * a whole bunch of predined method names that map to operators
 */


//spaceship, does a.compareTo(b)
String aString = "fred"
String bString = "brett"

println aString <=> bString

//to overide an operator in your class, declare the corresponding method
//eg +a is a.unaryPlus()

@Immutable
class OpsStuff {
	
	
	Integer myVal = 0
	

	
	def plus(OpsStuff b) {
		return new OpsStuff(myVal: this.myVal + b.getMyVal())
	}
}

OpsStuff myOps = new OpsStuff(100)
println myOps.myVal
def newOpsStuff = myOps + new OpsStuff(50)
println newOpsStuff.myVal
assert newOpsStuff == new OpsStuff(150)

@Immutable           //overrides == operator
class Money {
	int amount
	String currency
	
	Money plus( Money other) {
		if (null == other) return this
		if (other.currency != currency) {
			throw new IllegalArgumentException("$other.currency is not $currency")
		}
		return new Money(amount + other.amount, currency)
	}
	
	//overload the + operator
	Money plus(Integer more) {
		return new Money(amount + more, currency)
	}
}

Money buck = new Money(1, 'USD')
assert buck
assert buck == new Money(1, 'USD')
assert buck + buck == new Money(2, 'USD')
assert buck + 3 == new Money(4, 'USD')


//Strings
def multiLine = """I span multiple 
lines"""

println multiLine

//need to do this to get a character
char meChar = 'x'
Character alsoMeChar = 'x'
int meInt = "55".toInteger()
long meLong = "555".toLong()
double meDub = "55.678".toDouble()

TimeZone.default = TimeZone.getTimeZone('GMT')
Date date = new Date(0)
//def dateMap = [y:date[YEAR]- 1900, m:date[MONTH], d:date[DAY_OF_MONTH]]
//def out = "Year $dateMap.y Month $dateMap.m Day $dateMap.d"
//assert out == 'Year 70 Month 0 Day 1'

def tz = TimeZone.getTimeZone('GMT')
def format = 'd MMM YYYY HH:mm:SS z'
//def out = "Date is ${date.format(format, tz)}"

def daYear = 1900

def sql = """
SELECT * FROM MyTable
	WHERE Year = $daYear
"""

//with double quote, need to escape $ sign
def myEsc = "my 0.02\$"

//gstrings are not strings but groovy will coerce them automatically

//stringbuffer is created when  << is used on a string
def greeting = 'Hello'
//<< and assign turns string into strinbuffer
greeting <<= ' Groovy'
assert greeting instanceof java.lang.StringBuffer
greeting << '!'
greeting[1..4] = 'i'
println greeting


//regular expression support
//find =~
//match ==~
//pattern ~string
assert "abc"  == /abc/
assert "\\d" == /\d/ //no need to escape backslash in slashy string / /
def reference = "hello"
assert reference == /$reference/

/* pattern symbols
 * .			any character
 * ^			start of line
 * $			end of line
 * \d			digit character
 * \D			any character except digit
 * \s			whitespace character
 * \S			any char except whitespace
 * \w			word character
 * \W			any character except word
 * \b			word boundary
 * ()			grouping
 * ( x | y )	x or y, as in (Groovy|Java|Ruby)
 * \1			backmatch to group 1
 * x*			zero or more occurences of x
 * x+			one or more occurences of x
 * x?			zero or one occurrence of x
 * x {m, n}		at least m and at most n occurences of x
 * x{m}			exactly m occurences of x
 * [a-f]		character class containing a,b,c,d,e,f
 * [^a]			any character except a
 * (?is:x)		switch mode when evaluating x, i turns on ignore case, s means single line mode
 */


def twister = 'she sells sea shells at the sea shore of seychelles'

assert twister =~ /s.a/ //find operator, true if finds
def finder = (twister =~ /s.a/)
assert finder instanceof java.util.regex.Matcher

//contains only words delimited by single spaces
assert twister ==~ /(\w+ \w+)*/ //match operator

def WORD = /\w+/
matches = (twister ==~ /($WORD $WORD)*/)
assert matches instanceof java.lang.Boolean

assert !(twister ==~ /s.e/) //match is full, unlike find

def wordsByX = twister.replaceAll(WORD, 'x')

def words = twister.split(/ /)
assert words.size() == 10
assert words[0] == 'she'

//find evaluates to a Matcher, can be used in groovy conditional due to Groovy truth
//break up regex with constant expressions where possible

def myFairStringy = 'The rain in Spain stays mainly in the plain!'

def wordEnding = /\w*ain/
def rhyme = /\b$wordEnding\b/
def found = ''
myFairStringy.eachMatch(rhyme) { match ->
	found += match + ' '
}

assert found == 'rain Spain plain '

//note closure, implicit use of it for the variable, and the closure if last parameter can be outside the ()'s on the method call
def cloze = myFairStringy.replaceAll(rhyme) {
	it-'ain'+'___'
}
println cloze

//groovy adds array like access to Matcher
//and some other stuff to investigate later


//NUMBERS
//decimals default to BigDecimal
//coercion, when using operators on numbers
//float or double = double
//any BigDecimal = BigDecimal
//any BigInteger = BigInteger
//any Long = Long
//otherwise Integer

//intdiv can be used for integer result on division
//beware, if you divide two ints, you get a rounded int result, you must use floating point literal ie f on one
//call methods on numbers ie
assert 1 == (-1).abs()
assert 2 == 2.5.toInteger()
assert 2 == 2.5 as Integer
assert 2 == (int)2.5
assert 3 == 2.5f.round()
assert 3.142 == Math.PI.round(3)
assert 4 == 4.5f.trunc()
assert 2.718 == Math.E.trunc(3)
assert '2.718'.isNumber()
assert 5 == '5'.toInteger()
assert 5 == '5' as Integer
assert 53 == (int)'5' //a gotcha, don't cast string to number

//GDK also defines times, upto, downto, step
def store = ''
10.times { 
	store += 'x'
}

store = ''

1.upto(5) { number ->
	store += number
}


store  = ''
2.downto(-2) { number -> 
	store += number + ' '
}

store = ''
0.step(0.5, 0.1) { number ->
	store += number + ' '
}


//remember types are resolved at runtime, not compile time




















































