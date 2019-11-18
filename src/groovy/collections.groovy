package groovy

import groovy.transform.Sortable
import groovy.transform.ToString
import java.util.stream.Collectors

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
//collect is called map in other languages
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

//remove duplicates in list, create a set from the list
def x = [1,1,1]
def xclear = new HashSet(x).toList()
println xclear
//or use the unique method
def xunique = x.unique()
println xunique

//removing nulls from a list
def xnulls = [1, null, 1]
//note, not parens required if only single argument
def xnon = xnulls.findAll {it != null}
println xnon
//or use grep, which is passed a "filter object"
xnon = xnulls.grep {it}
println xnon

println sortEggs.first()
println sortEggs.head()
//tail is all items following the head
//useful in recursion
println sortEggs.tail()
println sortEggs.last()
println sortEggs.count(angus)
//max, min
//find returns the item, not a list
println sortEggs.find { it.code == angus.code}
//every, any
def names = ''
sortEggs.each { item ->
	names += "$item.code "
}
println names

def defRevNames = ''
sortEggs.reverseEach { item ->
	defRevNames += "$item.code "
}
println defRevNames

names = ''
sortEggs.eachWithIndex { item, index ->
	names += "$index:$item.code "
}
println names

//join

//fold/reduce is knows as inject
//a lot more, collate, collectMany, combinations, dropWhile, flatten, groupBy, permutations, take, transpose, withIndex
//asImmutable = java.Collections.unmodifiableList
//asSynchronized = Collections.synchronizedList

//quicksort example, can be used on any list item that supports <, =, >
def quickSort(list) {
	if (list.size() < 2) return list
	def pivot = list[list.size().intdiv(2)] //find middle item
	def left = list.findAll { item -> item < pivot}
	def middle = list.findAll { item -> item == pivot}
	def right = list.findAll { item -> item > pivot}
	return quickSort(left) + middle + quickSort(right)
}

assert quickSort([]) == []
assert quickSort([1]) == [1]
println quickSort([1,2,3])
println quickSort([4,2, 88])
//duck typed
println quickSort([1.0f, 'a', 10, null])
println quickSort('bca')
//as long as the type supports size(), getAt(index), and findAll it works with this quickSort method
//such is the convenience of "duck typing"


//filter, map, reduce style
def urls = [ 
	new URL('http', 'myshop.com', 80, "index.html"),	
	new URL('https', 'myshop.com', 443, "buynow.html"),
	new URL('ftp', 'myshop.com', 21, 'downloads')
]

//filter, map, sort, make into a string
assert urls
	.findAll {it.port < 99 }
	.collect { it.file.toUpperCase() }
	.sort()
	.join(', ') == 'DOWNLOADS, INDEX.HTML'
	
	
//java 8 style
//note the use of some groovy-ism's combined with java style

def commaSep = Collectors.joining(", ")	
assert urls.stream()
	.filter {it.port < 99}
	.map { it.file.toUpperCase()}
	.sorted()
	.collect(commaSep) == 'DOWNLOADS, INDEX.HTML'



//maps
def myMap = [a:1, b:2, c:3]
assert myMap instanceof LinkedHashMap
assert myMap.size() == 3
assert myMap['a'] == 1

def emptyMap = [:]
assert emptyMap.size() == 0
def explicitMap = new TreeMap()
explicitMap.putAll(myMap)
assert explicitMap['a'] == 1
def composed = [x: 'y', *:myMap]
assert composed == [x:'y', a:1, b:2, c:3]

//for string keys, can leave out the quotes ' or "
//might need to put variable in () when using as a key to force evaluation
assert myMap.a == 1
println myMap.get('a')
println myMap.get('a', 0)
myMap['d'] = 1

//iterate map
myMap.each {key, value ->
	println "Key: $key Value: $value"
}

//no key, value is an entry
myMap.each { entry ->
	println entry.value
}

//find, findAll, subMap, collect


































