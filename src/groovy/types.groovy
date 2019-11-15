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







































