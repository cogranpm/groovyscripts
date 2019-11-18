package groovy

import groovy.transform.Sortable
import groovy.transform.ToString

//ranges
assert (0..10).contains(10)
assert (0..<10).contains(9)
assert (0..<10).contains(10) == false

//explicit construction
def a = new IntRange(0,10)

//date range


//string range
assert('a'..'c').contains('b')

//for in range loop
def log = ''
for (element in 5..9) {
	log += element
}

println log

//.each is an alternative
log = ''
(9..<5).each { element ->
	log += element
}

//important methods on a range
//each and contains

//can be used in switches
def age = 36
switch (age) {
	case 16..20 : println 'young'; break
	case 21..50 : println 'old'; break
	default: println 'really young or old'
}


def ages = [20, 36, 42, 56]
def midage = 21..50
//can filter with a range
assert ages.grep(midage) == [36, 42]

//ranges very handy in the business world
//any datatype can be used with a range as long as it:
	// implements next and previous
	// implements compareTo

class Weekday implements Comparable {
	static final DAYS = [
		'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'
	]
	
	private int index = 0
	
	Weekday(String day) {
		index = DAYS.indexOf(day)
	}
	
	Weekday next() {
		return new Weekday(DAYS[(index + 1) % DAYS.size()])
	}
	
	Weekday previous() {
		return new Weekday(DAYS[index - 1])
	}
	
	int compareTo(Object other) {
		return this.index <=> other.index
	}
	
	String toString() {
		return DAYS[index]
	}
	
}

def mon = new Weekday('Mon')
def fri = new Weekday('Fri')

def worklog = ''
for (day in mon..fri) {
	worklog += day.toString() + ' '
}

println worklog


//LISTS
//are instances of java.util.ArrayList
List myList = [1,2,3]
assert myList.size()== 3
assert myList[0] == 1
assert myList instanceof ArrayList

List emptyList = []
assert emptyList.size() == 0
List longList = (0..1000).toList()
assert longList[555] == 555

List explicitList = new ArrayList()
explicitList.addAll(myList)
assert explicitList.size() == 3
explicitList[0] = 10
assert explicitList[0] == 10

//gdk gives toList method to all arrays, collection objects and strings

myList = ['a', 'b', 'c', 'd', 'e', 'f']
println myList[0..2]
println myList[0,2,4]
myList[0..2] = ['x', 'y', 'z']
myList[3..5] = []
myList[1..1] = [0, 1, 2] //adding elements
//-ve index count backward from end of list
//last entry with list[-1]
//also ranges [-3..-1] gets last three entries
//reversed range gives a reversed list, [4..0]

//adding and removing
myList = []
myList += 'a'
myList += ['b', 'c']
//leftshift is like append
myList << 'd' << 'e'
def newList = myList - 'b'
println myList
println newList

println "multiply a list: " + myList * 2


assert myList.isCase('a')
assert 'b' in myList

//empty lists are false
def anEmpty = []
if (anEmpty) assert false


//switch with list as the case, checks if item is in list
def candidate = 'c'
switch (candidate) {
	case myList : assert true; break
	default : assert false
}

//grep on a list, gives intersection
def grepList = ['x', 'a', 'z'].grep(myList)
println "grep on list: $grepList"

//for loop
def expr = ''
for (i in [1, '*', 5]) {
	expr += i
}
println expr


//list methods, all those in jdk (28)
assert [1, [2,3]].flatten() == [1,2,3]
assert [1,2,3].intersect([4,3,1]) == [3,1]
//returns true if intersection is empty
println ([1,2,3].disjoint([4,5,6]))

//treat like stack
//pop, 

//reverse, sort
def famousHawks = ['Dunstall', 'Brereton', 'Tuck']
println famousHawks.sort()


@Sortable
@ToString
class SortExample {
	String code
	String desc
	
	SortExample(code, desc) {
		this.code = code
		this.desc = desc
	}
}

//is a sort in place, yes unless you create an immutable list via asImmutable()
def filthy = new SortExample("filthy", "drums")
def angus = new SortExample("angus", "guitar")
def malcolm = new SortExample("malcolm", "ryhthm")
def sortEggs = [angus, filthy, malcolm]
sortEggs.sort({ da, db -> da.code <=> db.code})
println sortEggs
sortEggs.sort({da, db -> da.desc <=> db.desc})
println sortEggs

//@Sortable gives us automatice sort by field static methods
sortEggs.sort(SortExample.comparatorByCode())
println sortEggs
sortEggs.sort(SortExample.comparatorByDesc())
println sortEggs

sortEggs.remove(1)
println sortEggs

//remove by value
sortEggs.remove(filthy)
println sortEggs

sortEggs = [angus, malcolm, filthy]
//transform 1 list into another
def changeInstruments = sortEggs.collect {item -> 
	switch (item.desc) {
		case "drums": new SortExample(item.code, "bass guitar"); break
		case "ryhthm": new SortExample(item.code, "trumpet"); break
		case "guitar": new SortExample(item.code, "violin"); break
	} 
}

println changeInstruments

//findAll takes a closure
def angusList = changeInstruments.findAll({ SortExample item ->
	item.code == angus.code	
}
)

println angusList[0]

//groovy does not have arrays, use list of lists for multidimensional array



	


































