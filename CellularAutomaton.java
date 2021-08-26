
import java.lang.String;
import java.util.Random;
import javax.swing.JFrame;
import java.lang.Thread;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;

class Generator
{
	public static Random g = new Random();
}

class Status
{
	public static final int S = 0;
	public static final int I = 1;
	public static final int R = 2;
}

class Patogen
{
	public final double infectionRate;
	public final double recoveryRate;

	Patogen(double i, double r)
	{
		infectionRate = i;
		recoveryRate = r;
	}
}

class Cell
{
	public int Ns;
	public int Ni;
	public int Nr;
	public int E;

	public double c;

	public int tempS;
	public int tempI;
	public int tempR;

	Cell(int Ns, int Ni, int Nr, int E, double c)
	{
		this.Ns = Ns;
		this.Ni = Ni;
		this.Nr = Nr;
		this.E = E;
		this.c = c;

		tempS = Ns;
		tempI = Ni;
		tempR = Nr;
	}

	public int Type()
	{
		double ps = (double)Ns / (double)(Ns + Ni + Nr), pi = (double)Ni / (double)(Ns + Ni + Nr), pr = (double)Nr / (double)(Ns + Ni + Nr);

		double v = Generator.g.nextDouble();

		if(v < pr)
			return Status.S;

		v -= pr;

		if(v < pi)
			return Status.I;

		return Status.R;
	}

	public void exchange(final Cell neighbour)
	{
		int E = this.E;
		if(E > neighbour.E)
			E = neighbour.E;

		int s;

		for(int i = 0; i < E; i++)
		{
			s = Type();

			switch(s)
			{
				case Status.S:
					if(this.tempS > 0)
					{
						neighbour.tempS += 1;
						this.tempS -= 1;
					} else if(this.tempI > 0)
					{
						neighbour.tempI += 1;
						this.tempI -= 1;
					}
					else
					{
						neighbour.tempR += 1;
						this.tempR -= 1;
					}
					break;
				case Status.I:
					if(this.tempI > 0)
					{
						neighbour.tempI += 1;
						this.tempI -= 1;
					} else if(this.tempS > 0)
					{
						neighbour.tempS += 1;
						this.tempS -= 1;
					}
					else
					{
						neighbour.tempR += 1;
						this.tempR -= 1;
					}
					
					break;
				case Status.R:
					if(this.tempR > 0)
					{
						neighbour.tempR += 1;
						this.tempR -= 1;
					} else if(this.tempS > 0)
					{
						neighbour.tempS += 1;
						this.tempS -= 1;
					}
					else
					{
						neighbour.tempI += 1;
						this.tempI -= 1;
					}
					break;
				default:
					break;
			}
		}
	}

	public void evolve(Patogen p)
	{
		int s = tempS, i = tempI, r = tempR;
		double ps = (double)s / (double)(s + i + r), pi = (double)i / (double)(s + i + r), pr = (double)r / (double)(s + i + r);
		int incr_S = 0, incr_I = 0, incr_R = 0;
		double v;

		for(int j = 0; j < i; j++)
		{
			for(int f = 0; f < c; f++)
			{
				v = Generator.g.nextDouble();
				if(v < ps*p.infectionRate)
				{
					incr_S--;
					incr_I++;
				}
			}

			v = Generator.g.nextDouble();
			if(v < p.recoveryRate)
			{
				incr_I--;
				incr_R++;
			}
		}

		tempS += incr_S;
		tempI += incr_I;
		tempR += incr_R;

		Ns = tempS;
		Ni = tempI;
		Nr = tempR;
	}
}

class MyCanvas extends Canvas
{
	Cell[][] A;
	int H;
	int W;

	int h;
	int w;

	double k = 255;

	final int MAX_P = 1050;

	public void addAutomaton(int H, int W, Cell[][] A)
	{
		this.H = H;
		this.W = W;
		this.A = A;
	}

	static int max(int n1, int n2)
	{
		if(n1 >= n2)
			return n1;

		return n2;
	}

	static int min(int n1, int n2)
	{
		if(n1 <= n2)
			return n1;

		return n2;
	}

	public void paint(Graphics g)
	{
		h = getHeight();
		w = getWidth();
		for(int i = 0; i < H; i++)
			for(int j = 0; j < W; j++)
			{
				g.setColor(new Color(255, 255 - min(max(0, (int)(((double)A[i][j].Ni*k)/(double)MAX_P)), 255), 255 - min(max(0, (int)(((double)A[i][j].Ni*k)/(double)MAX_P)), 255)));
				g.fillRect((int)((double)(j)*((double)w/(double)W)), (int)((double)(i)*((double)h/(double)H)), (int)((double)(j+1)*((double)w/(double)W))+1, (int)((double)(i+1)*((double)h/(double)H))+1);
			}
	}
}

class CellularAutomaton
{
	public static int H = 50;
	public static int W = 50;
	public static Cell[][] automaton = new Cell[H][W];

	static Patogen v = new Patogen(0.2, 0.5);
	

	static boolean isInside(int i, int j)
	{
		if(i < 0 || i >= H || j < 0 || j >= W)
			return false;

		return true;
	}

	static void city(int cI, int cJ)
	{
		for(int i = cI - 2; i <= cI + 2; i++)
		{
			for(int j = cJ - 2; j <= cJ + 2; j++)
			{
				automaton[i][j] = new Cell(1000, 0, 0, 30, 10);
			}
		}
	}

	static void street(int pI, int pJ, int aI, int aJ)
	{
		for(int i = pI; i <= aI; i++)
		{
			automaton[i][pJ] = new Cell(100, 0, 0, 24, 10);
		}

		for(int j = pJ; j <= aJ; j++)
		{
			automaton[aI][j] = new Cell(100, 0, 0, 24, 10);
		}
	}

	static void background()
	{
		for(int i = 0; i < H; i++)
			for(int j = 0; j < W; j++)
				automaton[i][j] = new Cell(30, 0, 0, 1, 2);
	}

	static final int[][] neighbourhood = {{-1, 0, 1, 0}, {0, 1, 0, -1}};

	static void update()
	{
		for(int i = 0; i < H; i++)
		{
			for(int j = 0; j < W; j++)
			{
				for(int y = 0; y < 4; y++)
					for(int x = 0; x < 4; x++)
						if(isInside(i + neighbourhood[0][y], j + neighbourhood[1][x]))
							automaton[i][j].exchange(automaton[i + neighbourhood[0][y]][j + neighbourhood[1][x]]);
			}
		}

		for(int i = 0; i < H; i++)
		{
			for(int j = 0; j < W; j++)
			{
				automaton[i][j].evolve(v);
			}
		}
	}

	public static void main(String args[])
	{

		background();
		city(10, 10);
		city(40, 40);
		city(40, 10);
		street(13, 10, 37, 10);

		automaton[40][10] = new Cell(900, 100, 0, 30, 10);


		MyCanvas m = new MyCanvas(); 
		m.addAutomaton(H, W, automaton);
        JFrame f=new JFrame();  
        f.add(m); 
		f.setSize(500,500);
		f.setVisible(true);


		while(true)
		{
			update();
			
			m.repaint();
			f.repaint();
			try{
			Thread.sleep(500);
			}catch(InterruptedException e){}
		}
	}
}
