# Agoda & VietJet Test Cases

## Agoda Test Cases (Step-by-Step with Expected Behavior)

### TC 01: Search and Sort Hotel Successfully
**Step 1:** Navigate to https://www.agoda.com/
- *Expected:* Agoda homepage is displayed.

**Step 2:** Search hotel with:
- Place: Da Nang
- Date: 3 days from next Friday
- Number of people: Family Travelers → 2 rooms, 4 adults
- *Expected:* Search result page shows hotels in Da Nang.

**Step 3:** Verify search result is displayed.
- *Expected:* First 5 hotels listed correctly with Da Nang as destination.

**Step 4:** Sort hotels by lowest prices.
- *Expected:* First 5 hotels re-ordered by ascending price, still showing Da Nang.

---

### TC 02: Search and Filter Hotel Successfully
**Step 1:** Navigate to https://www.agoda.com/
- *Expected:* Agoda homepage is displayed.

**Step 2:** Search hotel with:
- Place: Da Nang
- Date: 3 days from next Friday
- Number of people: Family Travelers → 2 rooms, 4 adults
- *Expected:* Search result page shows hotels in Da Nang.

**Step 3:** Verify search result is displayed.
- *Expected:* First 5 hotels listed correctly with Da Nang as destination.

**Step 4:** Apply filter:
- Price: 500,000–1,000,000 VND
- Star: 3
- *Expected:* Price and Star filters highlighted, first 5 hotels match criteria.

**Step 5:** Remove price filter.
- *Expected:* Price filter cleared, Star filter remains, results update accordingly.

---

### TC 03: Search, Filter, and Verify Hotel Details Successfully
**Step 1:** Navigate to https://www.agoda.com/
- *Expected:* Agoda homepage is displayed.

**Step 2:** Search hotel with:
- Place: Da Nang
- Date: 3 days from next Friday
- Number of people: Family Travelers → 2 rooms, 4 adults
- *Expected:* Search result page shows hotels in Da Nang.

**Step 3:** Verify search result is displayed.
- *Expected:* First 5 hotels listed correctly with Da Nang as destination.

**Step 4:** Filter hotels with swimming pool and choose the 5th hotel.
- *Expected:* Hotel detail page opens, showing correct info (Name, Destination, Swimming Pool).

**Step 5:** Go back to filter page.
- *Expected:* Returns to filtered results, swimming pool filter still applied.

**Step 6:** Hover over 1st hotel to show review points.
- *Expected:* Popup shows review categories: Cleanliness, Facilities, Service, Location, Value for money.

**Step 7:** Select 1st hotel.
- *Expected:* Hotel detail page opens, info matches popup.

---

## VietJet Test Cases (Step-by-Step with Expected Behavior)

### TC 01: Search and Choose Tickets on a Specific Day Successfully
**Step 1:** Navigate to https://www.vietjetair.com/Sites/Web/en-US/Home
- *Expected:* VietJet homepage is displayed in English.

**Step 2:** Search ticket with:
- "Return" flight from Ho Chi Minh to Ha Noi
- Departure: tomorrow
- Return: 3 days after departure
- Currency: VND
- Number of adults: 2
- *Expected:*
  - "Select Travel Options" page displayed
  - Ticket prices in VND
  - Flight dates correct
  - Departure/arrival places correct
  - Number of passengers correct

**Step 3:** Choose cheapest tickets and click "Continue"
- *Expected:* "Passenger Information" page displayed, ticket info correct

---

### TC 02: Search and Choose Cheapest Tickets in Next 3 Months Successfully
**Step 1:** Navigate to https://www.vietjetair.com/Sites/Web/vi-VN/Home
- *Expected:* VietJet homepage is displayed in Vietnamese.

**Step 2:** Search ticket with:
- "Khứ hồi" (Return) flight from Ho Chi Minh to Ha Noi
- Select "Tìm giá vé rẻ nhất?" (Find cheapest fare?)
- Currency: VND
- Number of adults: 2
- *Expected:*
  - "Chọn giá vé" (Select Fare) page displayed
  - Flight info correct

**Step 3:** Choose cheapest tickets for a 3-day trip in next 3 months and click "Tiếp tục" (Continue)
- *Expected:* "Lựa chọn chuyến đi" (Select Travel Options) page displayed, ticket info correct

**Step 4:** Choose cheapest tickets and click "Continue"
- *Expected:* "Passenger Information" page displayed, ticket info correct
