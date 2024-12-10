from ortools.constraint_solver import pywrapcp, routing_enums_pb2
import time
import os
import csv

def read_data_from_file(file_path):
    """Reads distance matrix and time windows from a file."""
    with open(file_path, 'r') as file:
        # Read the number of cities
        num_cities = int(file.readline().strip())
        
        # Initialize distance matrix
        distance_matrix = []
        for _ in range(num_cities):
            row = list(map(int, file.readline().strip().split()))
            distance_matrix.append(row)
        
        # Initialize time windows
        time_windows = []
        for _ in range(num_cities):
            time_window = file.readline().strip().split()
            if len(time_window) == 2:
                # Convert time windows to tuples
                time_windows.append((int(time_window[0]), int(time_window[1])))
            else:
                # Append None if no time window is specified
                time_windows.append(None)

    return {'distance_matrix': distance_matrix, 'time_windows': time_windows}, num_cities


def create_data_model(file_path):

    """Read data from txt files"""
    data_from_file, num_cities = read_data_from_file(file_path)


    """Stores the data for the problem."""
    data = {
        'distance_matrix': data_from_file['distance_matrix'],
        'time_windows': data_from_file['time_windows'],

        'num_vehicles': 1,  # TSP uses only one vehicle
        'depot': 0  # Start and end at node 0
    }
    return data, num_cities

def solve_tsp_with_long_distance():
    """Solve the TSP with long distances and time windows."""


    file_path = 'C:/Users/hp/Desktop/Master IA/GraphComp/ORTools/Data/large/n101_instance1.txt' # change it for each file 
    
    data, num_cities = create_data_model(file_path)

    # Create the routing index manager
    manager = pywrapcp.RoutingIndexManager(
        len(data['distance_matrix']), data['num_vehicles'], data['depot']
    )

    # Create the routing model
    routing = pywrapcp.RoutingModel(manager)

    # Define cost function
    speed = 80  # Speed in km/h
    def distance_callback(from_index, to_index):
        from_node = manager.IndexToNode(from_index)
        to_node = manager.IndexToNode(to_index)
        distance = data['distance_matrix'][from_node][to_node]  # Distance in km
        travel_time = distance / speed  # Time in hours
        return int(travel_time * 60)  # Convert to minutes

    # Register the cost function
    transit_callback_index = routing.RegisterTransitCallback(distance_callback)
    routing.SetArcCostEvaluatorOfAllVehicles(transit_callback_index)

    # Add time dimension
    max_time = 10**9  # Large value to allow flexibility

    """Change this to 0, 30, 60, 120"""
    # waiting_time = 0
    # waiting_time = 30
    # waiting_time = 60
    waiting_time = 120

    time = "Time"
    routing.AddDimension(
        transit_callback_index,  # Travel time callback
        waiting_time,                      # Waiting time
        max_time,                # Maximum cumulative time
        True,                    # Force start time to zero
        time                     # Dimension name
    )
    time_dimension = routing.GetDimensionOrDie(time)



    # Add time window constraints to all cities with defined time windows
    for city_index, time_window in enumerate(data['time_windows']):
        if time_window is not None:  # Skip cities without time windows
            node_index = manager.NodeToIndex(city_index)

            # Calculate travel time to the city (from Depot)
            travel_time_to_city = data['distance_matrix'][0][city_index] / speed * 60
            start_time = time_window[0] + (travel_time_to_city // 1440) * 1440
            end_time = time_window[1] + (travel_time_to_city // 1440) * 1440

            # Apply the time window
            time_dimension.CumulVar(node_index).SetRange(
                int(start_time), int(end_time)
            )

        
            

    # Set search parameters
    search_parameters = pywrapcp.DefaultRoutingSearchParameters()
    search_parameters.first_solution_strategy = (
        routing_enums_pb2.FirstSolutionStrategy.PATH_CHEAPEST_ARC
    )

    
    # Uncomment this if you want to limi the execution time 
    # search_parameters.time_limit.FromSeconds(1600)  # 5400 90-minutes time limit

    # Solve the problem
    solution = routing.SolveWithParameters(search_parameters)

    # Print solution
    if solution:
        total_dis = solution.ObjectiveValue()
        print(f"Objective: {total_dis}")
        index = routing.Start(0)
        print('Route:')
        plan_output = ''
        while not routing.IsEnd(index):
            time_var = time_dimension.CumulVar(index)
            plan_output += f'{manager.IndexToNode(index)} Time({solution.Min(time_var)},{solution.Max(time_var)}) -> '
            index = solution.Value(routing.NextVar(index))
        time_var = time_dimension.CumulVar(index)
        plan_output += f'{manager.IndexToNode(index)} Time({solution.Min(time_var)},{solution.Max(time_var)})'
        print(plan_output)              
    else:
        plan_output = None
        total_dis = None
        print("No solution found!")

    return ["Complete", # Method type
            num_cities, # Number of cities
            plan_output, # Route/path 
            total_dis, # Total distance 
            None, # Execution time 
            file_path.split('/')[8], # Category (small, medium, large)
            None, # Epsilon (pour les méthode incomplètes)
            waiting_time # Waiting time
            ]

# get the execution time
start_time = time.time()
solution = solve_tsp_with_long_distance()
end_time = time.time()

exec_time = (end_time - start_time) * 1000

print("Time", exec_time)
solution[4] = exec_time
print("Solution:", solution)



"""Store the results"""
output_path = 'result/tsp_results.csv'
# Ensure the file exists (create it if not)
if not os.path.exists(output_path):
    with open(output_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        # Write the header row
        writer.writerow(['Method', 'Num Cities', 'Route', 'Total distance', 'Execution Time (ms)', 'Category', 'Epsilon', 'Waiting Time'])

# Open the file in append mode to add new results
with open(output_path, mode='a', newline='') as file:
    writer = csv.writer(file)
    # Append the result row
    writer.writerow(solution)
