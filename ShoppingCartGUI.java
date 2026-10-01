/* Name: Glenn Flinchum
Course: CNT 4714 – Summer 2025
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday June 1, 2025
*/
package eventDrivenProgramming;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // Keep this import for potential future use or if other parts of code implicitly rely on it

public class ShoppingCartGUI {

	private static final int WIDTH = 1000;
	private static final int LENGTH = 600;
	private static final double taxRate  = 0.06;

	private JLabel blankLabel, controlsLabel, shoppingCartLabel,
				   northLabel1, northLabel2, northLabel3, northLabel4;

	private static JTextField northTextField1, northTextField2, northTextField3, northTextField4,
					   centerTextField1, centerTextField2, centerTextField3, centerTextField4, centerTextField5;

	private JButton blankButton, searchB, addB, deleteB, emptyB, checkoutB, exitB;

	private SearchButtonHandler 	searchHandler;
	private AddButtonHandler 		addHandler;
	private DeleteButtonHandler 	deleteHandler;
	private EmptyButtonHandler 		emptyHandler;
	private CheckoutButtonHandler 	checkoutHandler;
	private ExitButtonHandler 		exitHandler;

	JFrame initialFrame = new JFrame("Shopping Cart");

	static int itemCount = 0;
	static final int maxItemCount = 5;

	static String[] cartGUI = new String[maxItemCount];

	static String[] cartTransaction = new String[maxItemCount];

	static double subtotal = 0.0;


    public ShoppingCartGUI() {
        initialFrame.setSize(WIDTH, LENGTH);
        initialFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialFrame.setLocationRelativeTo(null);


        blankButton = new JButton(" ");
        blankLabel = new JLabel(" ", SwingConstants.RIGHT);

        controlsLabel = new JLabel(" USER CONTROLS ", SwingConstants.RIGHT);

        searchB 	  = new JButton("Search For Item #" + (itemCount + 1));
        searchHandler = new SearchButtonHandler();
        searchB.addActionListener(searchHandler);

        addB 		  = new JButton("Add Item #" + (itemCount + 1));
        addHandler 	  = new AddButtonHandler();
        addB.addActionListener(addHandler);

        deleteB 	  = new JButton("Delete Last Item Added To Cart");
        deleteHandler = new DeleteButtonHandler();
        deleteB.addActionListener(deleteHandler);

        emptyB 		  = new JButton("Empty Cart");
        emptyHandler  = new EmptyButtonHandler();
        emptyB.addActionListener(emptyHandler);

        checkoutB	  = new JButton("Check Out");
        checkoutHandler = new CheckoutButtonHandler();
        checkoutB.addActionListener(checkoutHandler);

        exitB 		  = new JButton("Exit");
        exitHandler   = new ExitButtonHandler();
        exitB.addActionListener(exitHandler);

        searchB.setEnabled	(true);
        deleteB.setEnabled	(true);
        exitB.setEnabled	(true);

        Container pane = initialFrame.getContentPane();

        GridLayout grid6by2 = new GridLayout(6,2,8,4);
        GridLayout grid7by2 = new GridLayout(7,2,8,4);

        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();

        northPanel.setLayout(grid6by2);
        centerPanel.setLayout(grid7by2);
        southPanel.setLayout(grid6by2);

        pane.add(northPanel, BorderLayout.NORTH);
        pane.add(centerPanel, BorderLayout.CENTER);
        pane.add(southPanel, BorderLayout.SOUTH);

        pane.setBackground(Color.black);

        northPanel.setBackground(Color.GRAY);
        centerPanel.setBackground(Color.LIGHT_GRAY);
        southPanel.setBackground(Color.DARK_GRAY);

        initialFrame.setVisible(true);

        southPanel.add(controlsLabel);
        controlsLabel.setHorizontalAlignment(JLabel.CENTER);
        controlsLabel.setForeground(Color.white);
        southPanel.add(blankButton);
        blankButton.setVisible(false);

        southPanel.add(searchB);
        southPanel.add(addB);
        southPanel.add(deleteB);
        southPanel.add(checkoutB);
        southPanel.add(emptyB);
        southPanel.add(exitB);

        addB.setVisible(false);
        deleteB.setVisible(false);
        checkoutB.setVisible(false);


        IsEmpty statusChecker = new IsEmpty();
        shoppingCartLabel = new JLabel(statusChecker.getCartStatus(itemCount), SwingConstants.CENTER);
        shoppingCartLabel.setForeground(Color.red);


        centerPanel.add(shoppingCartLabel);

        centerTextField1 = new JTextField();
        centerTextField2 = new JTextField();
        centerTextField3 = new JTextField();
        centerTextField4 = new JTextField();
        centerTextField5 = new JTextField();

        centerPanel.add(centerTextField1);
        centerPanel.add(centerTextField2);
        centerPanel.add(centerTextField3);
        centerPanel.add(centerTextField4);
        centerPanel.add(centerTextField5);

        centerTextField1.setEditable(false);
        centerTextField2.setEditable(false);
        centerTextField3.setEditable(false);
        centerTextField4.setEditable(false);
        centerTextField5.setEditable(false);


        northLabel1 = new JLabel("Enter Item ID for Item #" + (itemCount + 1), SwingConstants.RIGHT);
        northLabel2 = new JLabel("Enter quantity for Item #" + (itemCount + 1), SwingConstants.RIGHT);
        northLabel3 = new JLabel("Details for Item #" + (itemCount + 1), SwingConstants.RIGHT);
        northLabel4 = new JLabel("Current Subtotal for " + (itemCount) + " items(s)", SwingConstants.RIGHT);

        northTextField1 = new JTextField();
        northTextField2 = new JTextField();
        northTextField3 = new JTextField();
        northTextField4 = new JTextField();

        northPanel.add(northLabel1);
        northPanel.add(northTextField1);

        northPanel.add(northLabel2);
        northPanel.add(northTextField2);

        northPanel.add(northLabel3);
        northPanel.add(northTextField3);

        northPanel.add(northLabel4);
        northPanel.add(northTextField4);


        northTextField3.setEditable(false);
        northTextField4.setEditable(false);


    }

    private class ExitButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}

    }

    private class CheckoutButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (itemCount == 0) {
                JOptionPane.showMessageDialog(null, "Your cart is empty. Nothing to checkout.", "ERROR: Checkout Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- Invoice Generation for JOptionPane ---
            StringBuilder invoiceMessage = new StringBuilder();
            LocalDateTime now = LocalDateTime.now();
            
            // Format for invoice: "DD/MM/YYYY HH:MM:SS TMZ"
            DateTimeFormatter invoiceDateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm:ss a"); // Changed to MM/DD/YYYY for example
            String invoiceDateTime = now.format(invoiceDateTimeFormatter) + " EDT"; // Append EDT directly

            invoiceMessage.append("Date: ").append(invoiceDateTime).append("\n");
            invoiceMessage.append("Number of line items: ").append(itemCount).append("\n\n");
            invoiceMessage.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:\n");

            double currentSubtotal = 0.0; // Recalculate subtotal for the invoice display

            for (int i = 0; i < itemCount; i++) {
                String[] item = cartTransaction[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String itemID = item[0].trim();
                String itemDesc = item[1].trim().replace("\"", ""); // Remove quotes for display
                double itemPriceEach = Double.parseDouble(item[2].trim());
                String discountPercentage = item[3].trim(); // This is like "0.10" or "0.00"
                int quantity = Integer.parseInt(item[4].trim());
                double itemTotalPrice = Double.parseDouble(item[5].trim());

                currentSubtotal += itemTotalPrice;

                // Format: 1. ID "Title" $Price Qty Discount% $Subtotal
                invoiceMessage.append(String.format("%d. %s \"%s\" $%.2f %d %s $%.2f\n",
                                        (i + 1), itemID, itemDesc, itemPriceEach, quantity, discountPercentage, itemTotalPrice));
            }

            invoiceMessage.append("\n");
            invoiceMessage.append(String.format("Order subtotal: $%.2f\n", currentSubtotal));
            invoiceMessage.append(String.format("Tax rate: %.0f%%\n", taxRate * 100));
            double taxAmount = currentSubtotal * taxRate;
            invoiceMessage.append(String.format("Tax amount: $%.2f\n", taxAmount));
            double orderTotal = currentSubtotal + taxAmount;
            invoiceMessage.append(String.format("ORDER TOTAL: $%.2f\n\n", orderTotal));
            invoiceMessage.append("Thanks for shopping at Glenn.com!");

            JOptionPane.showMessageDialog(null, invoiceMessage.toString(), "Nile Dot Com - FINAL INVOICE", JOptionPane.INFORMATION_MESSAGE);

            // --- Transaction Logging to transactions.csv ---
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {

                for (int i = 0; i < itemCount; i++) {
                    String[] item = cartTransaction[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String itemID = item[0].trim();
                    String itemDesc = item[1].trim();
                    String itemPrice = item[2].trim();
                    String discount = item[3].trim();
                    String quantity = item[4].trim();
                    String totalPrice = item[5].trim();


                    String timestampCompact = now.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
                    String longDate = now.format(DateTimeFormatter.ofPattern("MMMM dd,yyyy"));
                    String longTime = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                    String zone = "EDT";


                    String line = (timestampCompact + ", " +
                                   itemID + ", " +
                                   itemDesc + ", " +
                                   itemPrice + ", " +
                                   quantity + ", " +
                                   discount + ", " +
                                   totalPrice + ", " +
                                   longDate + ", " +
                                   longTime + ", " +
                                   zone);

                    writer.write(line);
                    writer.newLine();
                }
                writer.newLine(); // Add a blank line between transactions for readability

                writer.flush();
                // No need for a second message dialog here, the invoice one is sufficient.

                // Clear cart after checkout
                for (int i = 0; i < cartGUI.length; i++) {
                    cartGUI[i] = null;
                    cartTransaction[i] = null;
                }

                itemCount = 0;
                subtotal = 0.0; // Reset this globally too

                centerTextField1.setText("");
                centerTextField2.setText("");
                centerTextField3.setText("");
                centerTextField4.setText("");
                centerTextField5.setText("");

                northTextField1.setText("");
                northTextField2.setText("");
                northTextField3.setText("");
                northTextField4.setText("");

                northLabel1.setText("Enter Item ID for Item #1");
                northLabel2.setText("Enter quantity for Item #1");
                northLabel3.setText("Details for Item #1");
                northLabel4.setText("Current Subtotal for 0 item(s)");

                shoppingCartLabel.setText(new IsEmpty().getCartStatus(itemCount));

                searchB.setText("Search For Item #1");
                addB.setText("Add Item #1");
                searchB.setVisible(true);
                deleteB.setVisible(false);
                addB.setVisible(false);
                checkoutB.setVisible(false);
                emptyB.setVisible(true);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error writing to 'transactions.csv': " + ex.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private class EmptyButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            for (int i = 0; i < cartGUI.length; i++) {
                cartGUI[i] = null;
                cartTransaction[i] = null;
            }

            itemCount = 0;
            subtotal = 0.0;

            centerTextField1.setText("");
            centerTextField2.setText("");
            centerTextField3.setText("");
            centerTextField4.setText("");
            centerTextField5.setText("");

            northTextField1.setText("");
            northTextField2.setText("");
            northTextField3.setText("");
            northTextField4.setText("");

            northLabel1.setText("Enter Item ID for Item #1");
            northLabel2.setText("Enter quantity for Item #1");
            northLabel3.setText("Details for Item #1");
            northLabel4.setText("Current Subtotal for 0 item(s)");

            shoppingCartLabel.setText(new IsEmpty().getCartStatus(itemCount));

            searchB.setText("Search For Item #1");
            addB.setText("Add Item #1");
            searchB.setVisible(true);
            deleteB.setVisible(false);
            checkoutB.setVisible(false);
            addB.setVisible(false);
        }
    }


    private class DeleteButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (itemCount == 0) {
                JOptionPane.showMessageDialog(null, "Cart is already empty", "EMPTY CART", JOptionPane.WARNING_MESSAGE);
                return;
            }

            itemCount--;
            String lastItemGUI = cartGUI[itemCount];
            String lastItemTransaction = cartTransaction[itemCount];

            cartGUI[itemCount] = null;
            cartTransaction[itemCount] = null;

            switch (itemCount) {
                case 0: centerTextField1.setText(""); break;
                case 1: centerTextField2.setText(""); break;
                case 2: centerTextField3.setText(""); break;
                case 3: centerTextField4.setText(""); break;
                case 4: centerTextField5.setText(""); break;
            }

            try {
                String[] tokens = lastItemTransaction.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                double itemTotal = Double.parseDouble(tokens[5].trim());
                subtotal -= itemTotal;

            } catch (Exception ex) {
                System.out.println("Error parsing subtotal from removed cart item: " + ex.getMessage());
            }

            northTextField4.setText(String.format("$%.2f", subtotal));
            northLabel1.setText("Enter Item ID for Item #" + (itemCount + 1));
            northLabel2.setText("Enter quantity for Item #" + (itemCount + 1));
            northLabel3.setText("Details for Item #" + (itemCount + 1));
            northLabel4.setText("Current Subtotal for " + itemCount + " item(s)");
            shoppingCartLabel.setText(new IsEmpty().getCartStatus(itemCount));

            searchB.setText("Search For Item #" + (itemCount + 1));
            addB.setText("Add Item #" + (itemCount + 1));

            if (itemCount == 0) {
                deleteB.setVisible(false);
                checkoutB.setVisible(false);
                addB.setVisible(false);
                searchB.setVisible(true);
            }
        }
    }


    private class SearchButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {


			String itemID = northTextField1.getText().trim();
			String itemQuantity = northTextField2.getText().trim();

			if (itemID.isBlank() || itemQuantity.isBlank()) {
				JOptionPane.showMessageDialog(null, "Please enter item id and item quantity", "ERROR: INCORRECT INPUT", JOptionPane.ERROR_MESSAGE);
				return;
			}

			InventorySearch.searchInventory(itemID, itemQuantity);

			addB.setVisible(true);
		}

    }

    private class AddButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (itemCount < maxItemCount) {
	            String currentItemGUI = cartGUI[itemCount];

	            switch (itemCount) {
	                case 0: centerTextField1.setText(currentItemGUI); break;
	                case 1: centerTextField2.setText(currentItemGUI); break;
	                case 2: centerTextField3.setText(currentItemGUI); break;
	                case 3: centerTextField4.setText(currentItemGUI); break;
	                case 4: centerTextField5.setText(currentItemGUI); break;
		            }
	            try {
	                String transactionString = cartTransaction[itemCount];
	                String[] tokens = transactionString.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	                double itemTotal = Double.parseDouble(tokens[5].trim());
	                subtotal += itemTotal;

	            } catch (Exception ex) {
	                System.out.println("Error parsing subtotal from cart item: " + ex.getMessage());
	            }

	            northTextField4.setText(String.format("$%.2f", subtotal));

			itemCount++;

            northLabel1.setText("Enter Item ID for Item #" + (itemCount + 1));
            northLabel2.setText("Enter quantity for Item #" + (itemCount + 1));
            northLabel3.setText("Details for Item #" + (itemCount + 1));
            northLabel4.setText("Current Subtotal for " + (itemCount) + " item(s)");
            shoppingCartLabel.setText(new IsEmpty().getCartStatus(itemCount));

            northTextField1.setText("");
            northTextField2.setText("");
            northTextField3.setText("");


            searchB.setText("Search For Item #" + (itemCount + 1));
            addB.setText("Add Item #" + (itemCount + 1));
            searchB.setVisible(true);
            addB.setVisible(false);
            deleteB.setVisible(true);
            checkoutB.setVisible(true);
			}

			else {
				JOptionPane.showMessageDialog(null, "Cart is full (max 5 items)", "CART FULL", JOptionPane.WARNING_MESSAGE);
        	}
		}

    }


    public static class IsEmpty {
    	public String getCartStatus (int itemCount) {
    		if (itemCount < 1) {
    			return "Your Shopping Cart Is Currently Empty";
    		}

    	return "Your Shopping Cart Currently Contains " + (itemCount) + " Item(s)";
    	}
    }

public class InventorySearch {
    public static void searchInventory(String itemIDField, String itemQuantityField) {

    	File inFile = new File("inventory.csv");
    	FileReader inFileReader = null;
    	BufferedReader inBuffReader = null;

    	String inventoryLine = "";

    	boolean found = false;

    	try {
    		inFileReader = new FileReader(inFile);
    		inBuffReader = new BufferedReader(inFileReader);

    		inventoryLine = inBuffReader.readLine();

    		searchloop:while(inventoryLine != null) {
    			String[] tokens = inventoryLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    			String itemID = tokens[0].trim();
    			String itemDesc = tokens[1].trim();
    			String itemQuantityInStock = tokens[3].trim();
    			String itemPrice = tokens[4].trim();


    			if(itemID.equals(itemIDField)) {
    				System.out.println("FOUND ITEM ID: " + itemID);
    				found = true;

    				int itemQuantityFieldInt = Integer.parseInt(itemQuantityField);
                    int itemQuantityInt = Integer.parseInt(itemQuantityInStock);
                    double itemPriceDouble = Double.parseDouble(itemPrice);
                    double discountRate;

                    if (itemQuantityFieldInt >= 15) {
                        discountRate = 0.20;
                    } else if (itemQuantityFieldInt >= 10) {
                        discountRate = 0.15;
                    } else if (itemQuantityFieldInt >= 5) {
                        discountRate = 0.10;
                    } else {
                        discountRate = 0.00;
                    }

                    double itemTotalWithDiscount = itemPriceDouble * (1 - discountRate) * itemQuantityFieldInt;

                    String formattedTotalPrice = String.format("%.2f", itemTotalWithDiscount);
                    String formattedItemPrice = String.format("%.2f", itemPriceDouble);
                    String formattedDiscountRate = String.format("%.0f%%", discountRate * 100);


                    if (itemQuantityInt >= itemQuantityFieldInt) {
                    	System.out.println("ITEM IN STOCK (" + (itemQuantityInt-itemQuantityFieldInt) + " left in stock)");

                    	ShoppingCartGUI.northTextField3.setText(
                    		itemID + " " +
                    		itemDesc + " $" +
                    		formattedItemPrice + " " +
                    		itemQuantityFieldInt + " " +
                    		formattedDiscountRate + " $" +
                    		formattedTotalPrice);


                    	cartGUI[itemCount] =
                		("Item " + (itemCount+1) +
                		" - SKU: " + itemID +
                		", Desc: \"" + itemDesc + "\"" +
                		", Price Ea. $" + String.format("%.2f", itemPriceDouble) +
                		", Qty: " + itemQuantityFieldInt +
                		", Total: $" + formattedTotalPrice
                		);


                    	String quotedItemDesc = itemDesc.contains(",") ? "\"" + itemDesc + "\"" : itemDesc;
                    	cartTransaction[itemCount] =
                    	(itemID + "," +
                    	quotedItemDesc + "," +
                    	formattedItemPrice + "," +
                    	String.format("%.2f", discountRate) + "," + // Store actual decimal discount rate
                    	itemQuantityFieldInt + "," +
                    	formattedTotalPrice
                    	);


                    	break searchloop;
                    }
                    else {
                    	JOptionPane.showMessageDialog(null, "Item not in stock. Only " + itemQuantityInt + " available.", "ERROR: ITEM NOT IN STOCK", JOptionPane.ERROR_MESSAGE);
                    }
    			}

    				inventoryLine = inBuffReader.readLine();
    		}

    		if(found == false) {
    			JOptionPane.showMessageDialog(null, "Item not found", "ERROR: ITEM NOT FOUND", JOptionPane.ERROR_MESSAGE);
    		}

    	}
    	catch (FileNotFoundException e) {
    		JOptionPane.showMessageDialog(null, "Inventory file not found", "ERROR: FILE NOT FOUND", JOptionPane.ERROR_MESSAGE);
    	}

    	catch (IOException e) {
    		JOptionPane.showMessageDialog(null, "Problem reading file", "ERROR: READING FILE", JOptionPane.ERROR_MESSAGE);
    	}
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity or price format in inventory file or user input.", "ERROR: DATA FORMAT", JOptionPane.ERROR_MESSAGE);
        }

    	try {
			if (inFileReader != null) {
                inFileReader.close();
            }
            if (inBuffReader != null) {
                inBuffReader.close();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShoppingCartGUI());
    }
}