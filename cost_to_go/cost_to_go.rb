require 'sinatra'
require 'net/http'
require 'json'
require 'nokogiri'

def metre_to_km(metres)
	(metres.to_f / 1000.0).round(3)
end

def gal_to_lit(cost_per_gal)
	# Cost per litre in american
	(3.78541/cost_per_gal.to_f).round(2)
end

def gas_prices
	# american dollar per gallons
	gas_uri = URI.parse("http://www.fueleconomy.gov/ws/rest/fuelprices")
	gas_res = Net::HTTP.get(gas_uri)
	xml_gas_price = Nokogiri::XML(gas_res)
	gal_to_lit(xml_gas_price.xpath("/fuelPrices/midgrade")[0].text)
end

def google_api(from, to)
	dist_uri = URI.parse("http://maps.googleapis.com/maps/api/directions/json?origin=#{from}&destination=#{to}")
	dist_string_res = Net::HTTP.get(dist_uri)
	dist_json_res = JSON.parse(dist_string_res)

	# google api maps distances
	text_dist = dist_json_res["routes"][0]["legs"][0]["distance"]["text"]
	val_dist = dist_json_res["routes"][0]["legs"][0]["distance"]["value"]
end

get '/' do
	content_type :json
	# Make sure that from and to parameters exist
	return "Please supply from and to values" if params[:from].nil? || params[:to].nil?

	from = params[:from]
	to = params[:to]

	puts "from = #{from} and to = #{to}"

	# trip distance
	distance = google_api(from, to)	

	# dollars/litre
	gas_price = gas_prices
	# litres/100km / 100 to give fuel used per km
	efficiency = 13.8/100
	# cost to go 1km in dollars
	cost_per_km = (efficiency*gas_price).round(2)
	# fuel used
	fuel_used = (metre_to_km(distance) * efficiency).round(3)

	# trip cost
	trip_cost = (cost_per_km * metre_to_km(distance)).round(2)

  	return { :trip_cost => trip_cost, :distance_metres => distance, :distance_km => metre_to_km(distance), :fuel_used_litres =>  fuel_used }.to_json

end