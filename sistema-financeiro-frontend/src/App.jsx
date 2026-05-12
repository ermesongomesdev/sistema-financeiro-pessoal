import { useEffect, useState } from "react";
import {
  LayoutDashboard,
  Wallet,
  Tags,
  Repeat,
  Target,
  FileText,
  Bell,
  Settings,
  LogOut,
  Menu,
  Plus,
  TrendingUp,
  TrendingDown,
  CreditCard,
  Users,
} from "lucide-react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
} from "recharts";

const monthlyData = [
  { month: "Jan", receitas: 2200, despesas: 1300 },
  { month: "Fev", receitas: 2600, despesas: 1450 },
  { month: "Mar", receitas: 2400, despesas: 1700 },
  { month: "Abr", receitas: 3100, despesas: 1850 },
  { month: "Mai", receitas: 2500, despesas: 350 },
];

const categoryData = [
  { name: "Alimentação", value: 350 },
  { name: "Transporte", value: 180 },
  { name: "Lazer", value: 120 },
  { name: "Contas", value: 540 },
];

const COLORS = ["#22c55e", "#ef4444", "#3b82f6", "#f97316"];

const menuItems = [
  { id: "dashboard", label: "Dashboard", icon: LayoutDashboard },
  { id: "wallets", label: "Carteiras", icon: Wallet },
  { id: "categories", label: "Categorias", icon: Tags },
  { id: "transactions", label: "Transações", icon: Repeat },
  { id: "debts", label: "Dívidas", icon: CreditCard },
  { id: "goals", label: "Metas", icon: Target },
  { id: "reports", label: "Relatórios", icon: FileText },
  { id: "notifications", label: "Notificações", icon: Bell },
];

const accentOptions = [
  { name: "Verde", value: "#10b981" },
  { name: "Azul", value: "#3b82f6" },
  { name: "Roxo", value: "#a855f7" },
  { name: "Laranja", value: "#f97316" },
];

function App() {
  const [activePage, setActivePage] = useState("dashboard");
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [accentColor, setAccentColor] = useState(() => {
  return localStorage.getItem("accentColor") || "#10b981";
});

useEffect(() => {
  localStorage.setItem("accentColor", accentColor);
}, [accentColor]);


  return (
    <div
  className="min-h-screen bg-[#070A12] text-white"
  style={{ "--accent": accentColor }}
>

      <div className="flex">
        <aside
          className={`${
            sidebarOpen ? "w-72" : "w-20"
          } min-h-screen border-r border-white/10 bg-[#0B1020] p-4 transition-all duration-300 hidden md:flex flex-col`}
        >
          <div className="flex items-center justify-between mb-8">
            {sidebarOpen && (
              <div>
                <h1 className="text-2xl font-black tracking-tight">
                  Veltrium<span className="accent-text">Pay</span>
                </h1>
                <p className="text-xs text-slate-400 mt-1">
                  Gestão financeira pessoal
                </p>
              </div>
            )}

            <button
              onClick={() => setSidebarOpen(!sidebarOpen)}
              className="p-2 rounded-xl hover:bg-white/10 transition"
            >
              <Menu size={20} />
            </button>
          </div>

          <nav className="space-y-2 flex-1">
            {menuItems.map((item) => {
              const Icon = item.icon;
              const active = activePage === item.id;

              return (
                <button
                  key={item.id}
                  onClick={() => setActivePage(item.id)}
                  className={`w-full flex items-center gap-3 px-4 py-3 rounded-2xl transition ${
                    active
                      ? "accent-bg shadow-lg"
                      : "text-slate-300 hover:bg-white/10"
                  }`}
                >
                  <Icon size={20} />
                  {sidebarOpen && <span className="font-medium">{item.label}</span>}
                </button>
              );
            })}
          </nav>

          <div className="space-y-2">
            <button
            onClick={() => setActivePage("settings")}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-2xl transition ${
activePage === "settings"
? "accent-bg shadow-lg"
                : "text-slate-300 hover:bg-white/10"
}`}
                  >
<Settings size={20} />
                  {sidebarOpen && <span>Configurações</span>}
            </button>

            <button className="w-full flex items-center gap-3 px-4 py-3 rounded-2xl text-red-300 hover:bg-red-500/10">
              <LogOut size={20} />
              {sidebarOpen && <span>Sair</span>}
            </button>
          </div>
        </aside>

        <main className="flex-1 min-h-screen">
          <header className="sticky top-0 z-20 border-b border-white/10 bg-[#070A12]/80 backdrop-blur-xl">
            <div className="flex items-center justify-between px-6 py-5">
              <div>
                <p className="text-sm text-slate-400">Bem-vindo de volta</p>
                <h2 className="text-2xl font-bold">
                  {getPageTitle(activePage)}
                </h2>
              </div>

              <div className="flex items-center gap-3">
                <button className="hidden sm:flex items-center gap-2 rounded-2xl bg-white/10 px-4 py-3 text-sm hover:bg-white/15 transition">
                  <Users size={18} />
                  Minha Carteira
                </button>

                <button className="flex items-center gap-2 rounded-2xl accent-bg px-4 py-3 text-sm font-bold text-black hover:bg-emerald-300 transition">
                  <Plus size={18} />
                  Novo
                </button>
              </div>
            </div>
          </header>

          <section className="p-6">
            {activePage === "dashboard" && <Dashboard />}
            {activePage === "wallets" && <SimplePage title="Carteiras" description="Gerencie carteiras individuais ou compartilhadas com outra pessoa." />}
            {activePage === "categories" && <SimplePage title="Categorias" description="Crie categorias de receitas e despesas com cores personalizadas." />}
            {activePage === "transactions" && <SimplePage title="Transações" description="Cadastre entradas, saídas, contas pagas e pendentes." />}
            {activePage === "debts" && <SimplePage title="Dívidas" description="Controle dívidas parceladas, credores e vencimentos." />}
            {activePage === "goals" && <SimplePage title="Metas" description="Acompanhe objetivos financeiros com progresso visual." />}
            {activePage === "reports" && <SimplePage title="Relatórios" description="Visualize relatórios mensais e exporte PDF ou Excel." />}
            {activePage === "notifications" && <SimplePage title="Notificações" description="Veja contas próximas do vencimento e alertas importantes." />}
            {activePage === "settings" && (<SettingsPage accentColor={accentColor} setAccentColor={setAccentColor}/>)}
          </section>
        </main>
      </div>
    </div>
  );
}

function Dashboard() {
  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-5">
        <MetricCard
          title="Saldo atual"
          value="R$ 2.150,00"
          icon={Wallet}
          description="+12% em relação ao mês anterior"
          positive
        />

        <MetricCard
          title="Receitas"
          value="R$ 2.500,00"
          icon={TrendingUp}
          description="Entradas confirmadas"
          positive
        />

        <MetricCard
          title="Despesas"
          value="R$ 350,00"
          icon={TrendingDown}
          description="Saídas registradas"
        />

        <MetricCard
          title="Dívidas"
          value="R$ 1.200,00"
          icon={CreditCard}
          description="6 parcelas pendentes"
        />
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
        <div className="xl:col-span-2 rounded-3xl border border-white/10 bg-white/5 p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-xl font-bold">Fluxo mensal</h3>
              <p className="text-sm text-slate-400">
                Comparação entre receitas e despesas
              </p>
            </div>
          </div>

          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={monthlyData}>
                <XAxis dataKey="month" stroke="#94a3b8" />
                <YAxis stroke="#94a3b8" />
                <Tooltip
                  contentStyle={{
                    background: "#0B1020",
                    border: "1px solid rgba(255,255,255,0.1)",
                    borderRadius: "16px",
                    color: "#fff",
                  }}
                />
                <Area
                  type="monotone"
                  dataKey="receitas"
                  stroke="#22c55e"
                  fill="#22c55e"
                  fillOpacity={0.18}
                />
                <Area
                  type="monotone"
                  dataKey="despesas"
                  stroke="#ef4444"
                  fill="#ef4444"
                  fillOpacity={0.14}
                />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
          <h3 className="text-xl font-bold">Gastos por categoria</h3>
          <p className="text-sm text-slate-400 mb-6">
            Distribuição das despesas
          </p>

          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={categoryData}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={90}
                  paddingAngle={4}
                  dataKey="value"
                >
                  {categoryData.map((_, index) => (
                    <Cell key={index} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip
                  contentStyle={{
                    background: "#0B1020",
                    border: "1px solid rgba(255,255,255,0.1)",
                    borderRadius: "16px",
                    color: "#fff",
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="space-y-3 mt-4">
            {categoryData.map((item, index) => (
              <div key={item.name} className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span
                    className="w-3 h-3 rounded-full"
                    style={{ backgroundColor: COLORS[index % COLORS.length] }}
                  />
                  <span className="text-sm text-slate-300">{item.name}</span>
                </div>
                <span className="text-sm font-bold">R$ {item.value}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
        <div className="flex items-center justify-between mb-5">
          <div>
            <h3 className="text-xl font-bold">Últimas transações</h3>
            <p className="text-sm text-slate-400">
              Movimentações recentes da sua carteira
            </p>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-white/10 text-sm text-slate-400">
                <th className="py-4">Descrição</th>
                <th>Categoria</th>
                <th>Data</th>
                <th>Status</th>
                <th className="text-right">Valor</th>
              </tr>
            </thead>

            <tbody>
              <TableRow
                description="Salario do mes"
                category="Receita"
                date="11/05/2026"
                status="Pago"
                amount="+ R$ 2.500,00"
                positive
              />

              <TableRow
                description="Compra no mercado"
                category="Alimentação"
                date="11/05/2026"
                status="Pago"
                amount="- R$ 350,00"
              />

              <TableRow
                description="Celular parcelado"
                category="Dívida"
                date="15/05/2026"
                status="Pendente"
                amount="- R$ 200,00"
              />
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

function MetricCard({ title, value, icon: Icon, description, positive }) {
  return (
    <div className="rounded-3xl border border-white/10 bg-white/5 p-6 hover:bg-white/[0.07] transition">
      <div className="flex items-center justify-between mb-5">
        <div
          className={`w-12 h-12 rounded-2xl flex items-center justify-center ${
            positive ? "accent-bg/15 accent-text" : "bg-red-400/15 text-red-300"
          }`}
        >
          <Icon size={22} />
        </div>

        <span
          className={`text-xs px-3 py-1 rounded-full ${
            positive ? "accent-bg/15 accent-text" : "bg-white/10 text-slate-300"
          }`}
        >
          Maio
        </span>
      </div>

      <p className="text-sm text-slate-400">{title}</p>
      <h3 className="text-3xl font-black mt-1">{value}</h3>
      <p className="text-xs text-slate-500 mt-3">{description}</p>
    </div>
  );
}

function TableRow({ description, category, date, status, amount, positive }) {
  return (
    <tr className="border-b border-white/5 text-sm">
      <td className="py-4 font-medium">{description}</td>
      <td className="text-slate-400">{category}</td>
      <td className="text-slate-400">{date}</td>
      <td>
        <span
          className={`px-3 py-1 rounded-full text-xs ${
            status === "Pago"
              ? "accent-bg/15 accent-text"
              : "bg-yellow-400/15 text-yellow-300"
          }`}
        >
          {status}
        </span>
      </td>
      <td
        className={`text-right font-bold ${
          positive ? "accent-text" : "text-red-300"
        }`}
      >
        {amount}
      </td>
    </tr>
  );
}

function SimplePage({ title, description }) {
  return (
    <div className="space-y-6">
      <div className="rounded-3xl border border-white/10 bg-white/5 p-8">
        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-5">
          <div>
            <h3 className="text-3xl font-black mb-3">{title}</h3>
            <p className="text-slate-400 leading-relaxed max-w-2xl">
              {description}
            </p>
          </div>

          <button className="flex items-center justify-center gap-2 rounded-2xl accent-bg px-5 py-3 text-sm font-bold text-black hover:bg-emerald-300 transition">
            <Plus size={18} />
            Adicionar novo
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
        <div className="xl:col-span-2 rounded-3xl border border-white/10 bg-white/5 p-6 min-h-360">
          <h4 className="text-xl font-bold mb-2">Registros</h4>
          <p className="text-sm text-slate-400 mb-6">
            Os dados desta página serão carregados diretamente do backend Java.
          </p>

          <div className="rounded-2xl border border-white/10 bg-black/20 p-5">
            <p className="text-slate-300 text-sm">
              Nenhum registro carregado ainda.
            </p>
          </div>
        </div>

        <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
          <h4 className="text-xl font-bold mb-2">Resumo</h4>
          <p className="text-sm text-slate-400 mb-6">
            Visão rápida do módulo selecionado.
          </p>

          <div className="space-y-4">
            <div className="rounded-2xl bg-white/5 p-4">
              <p className="text-xs text-slate-400">Status</p>
              <p className="text-lg font-bold accent-text">Ativo</p>
            </div>

            <div className="rounded-2xl bg-white/5 p-4">
              <p className="text-xs text-slate-400">Integração</p>
              <p className="text-lg font-bold">Backend Java</p>
            </div>

            <div className="rounded-2xl bg-white/5 p-4">
              <p className="text-xs text-slate-400">Segurança</p>
              <p className="text-lg font-bold">JWT</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function SettingsPage({ accentColor, setAccentColor }) {
  return (
    <div className="space-y-6">
      <div className="rounded-3xl border border-white/10 bg-white/5 p-8">
        <h3 className="text-3xl font-black mb-3">Configurações</h3>
        <p className="text-slate-400 max-w-2xl">
          Ajuste preferências da conta, aparência do painel e opções de segurança.
        </p>
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-5">
        <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
          <h4 className="text-xl font-bold mb-5">Perfil</h4>

          <div className="space-y-4">
            <div>
              <label className="text-sm text-slate-400">Nome</label>
              <input
                className="mt-2 w-full rounded-2xl border border-white/10 bg-black/30 px-4 py-3 outline-none focus:border-(--accent)"
                defaultValue="Ermeson"
              />
            </div>

            <div>
              <label className="text-sm text-slate-400">Email</label>
              <input
                className="mt-2 w-full rounded-2xl border border-white/10 bg-black/30 px-4 py-3 outline-none focus:border-(--accent)"
                defaultValue="ermeson@email.com"
              />
            </div>

            <button className="accent-bg rounded-2xl px-5 py-3 text-sm font-bold transition">
              Salvar alterações
            </button>
          </div>
        </div>

        <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
          <h4 className="text-xl font-bold mb-5">Aparência</h4>

          <div className="space-y-4">
            <div className="rounded-2xl border border-white/10 bg-black/20 p-4 flex items-center justify-between">
              <div>
                <p className="font-bold">Modo escuro</p>
                <p className="text-sm text-slate-400">
                  Interface premium com tema dark.
                </p>
              </div>

              <span className="rounded-full bg-emerald-400/20 px-3 py-1 text-xs text-emerald-300">
                Ativo
              </span>
            </div>

            <div className="rounded-2xl border border-white/10 bg-black/20 p-4">
              <p className="font-bold mb-1">Cor principal</p>
              <p className="text-sm text-slate-400 mb-4">
                Escolha a cor de destaque do painel.
              </p>

              <div className="flex gap-3">
                {accentOptions.map((color) => (
                  <button
                    key={color.value}
                    type="button"
                    onClick={() => setAccentColor(color.value)}
                    title={color.name}
                    className={`w-10 h-10 rounded-full border-2 transition hover:scale-110 ${
                      accentColor === color.value
                        ? "border-white ring-4 ring-white/20"
                        : "border-transparent"
                    }`}
                    style={{ backgroundColor: color.value }}
                  />
                ))}
              </div>

              <div className="mt-5 rounded-2xl border border-white/10 bg-black/30 p-4">
                <p className="text-xs text-slate-400">Cor selecionada</p>
                <p className="font-bold" style={{ color: accentColor }}>
                  {accentColor}
                </p>
              </div>
            </div>
          </div>
        </div>

        <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
          <h4 className="text-xl font-bold mb-5">Segurança</h4>

          <div className="space-y-4">
            <button className="w-full text-left rounded-2xl border border-white/10 bg-black/20 p-4 hover:bg-white/5 transition">
              Alterar senha
            </button>

            <button className="w-full text-left rounded-2xl border border-white/10 bg-black/20 p-4 hover:bg-white/5 transition">
              Gerenciar token JWT
            </button>
          </div>
        </div>

        <div className="rounded-3xl border border-red-500/20 bg-red-500/5 p-6">
          <h4 className="text-xl font-bold text-red-300 mb-3">Zona de risco</h4>
          <p className="text-sm text-slate-400 mb-5">
            Ações sensíveis da conta e da carteira financeira.
          </p>

          <button className="rounded-2xl bg-red-500 px-5 py-3 text-sm font-bold text-white hover:bg-red-400 transition">
            Sair da conta
          </button>
        </div>
      </div>
    </div>
  );
}

function getPageTitle(page) {
  const titles = {
    dashboard: "Dashboard",
    wallets: "Carteiras",
    categories: "Categorias",
    transactions: "Transações",
    debts: "Dívidas",
    goals: "Metas",
    reports: "Relatórios",
    notifications: "Notificações",
    settings: "Configurações",
  };

  return titles[page] || "Dashboard";
}

export default App;