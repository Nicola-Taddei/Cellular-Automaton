
# Cellular-Automaton
A simple implementation of an epidemic model based on SIR and cellular automatons with graphics.
The model attemps to predict the geographical evolution of an epidemic through time on the base of a some parameters (basically SIR model parameters) and a simple map of population density among a certain area. 
The model has not been tested against real world cases yet. So, for now, it's only made for fun :)
<br>
<br>
> :warning:

Since the project is still undergoing improvement work, it's not user friendly at all. It was originally designed to be used only by me, with the single purpose of trying to put in practice some knowledge.
Feel free to take part of the code, readapt it and maybe make it look a little bit more human :wink:
(By the way in the next future I will develope a nicer UI and improve the underlying model. I will be vary glad of any help you could give me)
<br>
<br>
> ## How does it work?
The model on which this software is based is built as follows: a square geographical [map](#map) divided in smaller squares ([cells](#cell)) which represent areas with uniform population density and frequency of contacts between people. 
They can be used to model on the map things as [cities](#cities), [streets](#streets), [agricoltural landscape or other inhabited areas](#in_areas).
<br>
<br>
> ## Map <a name='map' ></a>
The map consists of a **50x50** (measured in [cells](#cell)) area. For now the dimensions are hardcoded and can be changed by modifying the parameters of the CellularAutomaton class (see comments in the code).
Every turn (an arbitrary amount of time which acquires sense in relation to the other parameters) each cell of the map exchanges some inhabitants with the adjacent ones. The numer of elements exchanged depends on the population of both the cells interested in the operation.
<br>
<br>
> ## Cell <a name='cell'></a>
The Cell class is the basic unit of the model. It has a predefined and fixed population which evolves through time on the base of a [SIR model](https://en.wikipedia.org/wiki/Compartmental_models_in_epidemiology#The_SIR_model).
Despite the class is very flexible in the definition of population density, number of subjects exchanged with the neighbours on each turn and number of contacts of each inhabitant per turn (which are in fact arbitrary) I've thought of 3 standard environments, each represented by a function of the CellularAutomaton class:
<br>
>> ### Cities <a name='cities' ></a> :city:
*Population:* **1000**   <br>
*Exchanges:*    **30**   <br>
*Contacts:*     **10**   <br>

This cell type models a metropolitan environment with many people living in close contact to each other.
<br>

>> ### Streets <a name='streets' ></a> :train:
*Population:*  **100**   <br>
*Exchanges:*    **24**   <br>
*Contacts:*     **10**   <br>

This cell type models a generic transportation route.
<br>

>> ### Inhabited areas <a name='in_areas' ></a> :cactus:
*Population:*  **30**   <br>
*Exchanges:*    **1**   <br>
*Contacts:*     **2**   <br>

This cell type models a rural or inhabited area, with very low population and exchanges to the other cells.
<br>

