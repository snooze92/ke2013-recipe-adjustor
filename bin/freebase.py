from apiclient import discovery
from apiclient import model
import io, json

DEVELOPER_KEY = '' # your Google API key (optional)

model.JsonModel.alt_param = ""
freebase = discovery.build('freebase', 'v1', developerKey=DEVELOPER_KEY)

failedlist = []

def food_query(name, outfile):
    query = [{'mid': None, 'name': name, 'type': '/food/food', 'energy': None, \
          'nutrients': [{'/food/nutrition_fact/nutrient': None, \
                         '/food/nutrition_fact/quantity': None}]}]
    response = json.loads(freebase.mqlread(query=json.dumps(query)).execute())
    if len(response['result']) == 0:
        global failedlist
        print '?? No result for', name
        failedlist.append(name)
    
    for food in response['result']:
        name, energy, nutrients = get_attributes(food)
        ingredient = {}
        ingredient['name'] = name
        ingredient['glycemic index'] = 0
        try:
            ingredient['fiber'] = nutrients['Dietary fiber']
        except KeyError:
            ingredient['fiber'] = 0.0
        try:
            ingredient['carbohydrate'] = nutrients['Carbohydrate']
        except KeyError:
            ingredient['carbohydrate'] = 0.0
        try:
            ingredient['saturated fat'] = nutrients['Saturated fat']
        except KeyError:
            ingredient['saturated fat'] = 0.0
        try:
            ingredient['protein'] = nutrients['Protein']
        except KeyError:
            ingredient['protein'] = 0.0
        ingredient['kcal'] = energy
        json.dump(ingredient, outfile, indent=4, separators=(',', ': '))
        outfile.write(',\n')
        print name, 'succesfully appended'

def get_attributes(food):
    nutrients = {}
    name = food['name']
    energy = food['energy'] * 0.00239 # in kcal/g
    for nf in food['nutrients']:
        nname = nf[u'/food/nutrition_fact/nutrient']
        quant = nf[u'/food/nutrition_fact/quantity'] # in g/100g
        if quant != None: quant = quant * 0.01 # in g/g
        nutrients[nname] = quant

    return name, energy, nutrients

if __name__ == '__main__' :

    # make query queue
    queue = []
    rlist = ['../json/beetroot_chocolate_cake.json', \
               '../json/cheeseburger.json', \
               '../json/fried_rice.json', \
               '../json/fast_lasagne.json', \
               '../json/gingerbread_pancakes.json']
    for rpath in rlist:
        recipe = json.load(open(rpath))
        for ingr in recipe['ingredients']:
            queue.append(ingr['name'])
        
    queue_set = set(queue)
    queue = list(queue_set)
    queue.sort()
    print queue

    # execute and save
    outfile = open('ingredients.json', 'w')
    for food in queue:
        food_query(food, outfile)
    outfile.close()
    # the output needs to be manually copied into the knowledge base

    # show for which ingredients no data could be found
    print failedlist
