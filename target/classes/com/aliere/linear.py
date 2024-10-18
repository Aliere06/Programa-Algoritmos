seeds = [65, 89, 98, 3, 69]
xs = []
m = 100
seed_count = seeds.__len__()
i = seeds.__len__()
last = 0
next = 0
iterations = 0

for x in range(2):
    i += 1
        
    try:
        last = seeds[i - 2]
    except:
        last = xs[i - (seed_count + 2)]

    try:
        next = seeds[i - (seed_count + 1)]
    except:
        next = xs[i - ((seed_count * 2) + 1)]

    xs.append((last + next) % m)

while (i < 1000):
    i += 1
    
    try:
        last = seeds[i - 2]
    except:
        last = xs[i - (seed_count + 2)]
    
    try:
        next = seeds[i - (seed_count + 1)]
    except:
        next = xs[i - ((seed_count * 2) + 1)]
        
    xn = (last + next) % m
    
    if (xs[0] == xn) and (iterations == 0):
        break
    elif (xs.__len__() >= iterations) and (iterations != 0):
        break
    
    xs.append(xn)
    try:
        print(f"{last}+{next}({xs.index(next)}) -> {i}:{xs[i - (seed_count + 1)]}")
    except:
        print(f"{last}+{next}({seeds.index(next)}) -> {i}:{xs[i - (seed_count + 1)]}")
        
print(f"\n{xs}")