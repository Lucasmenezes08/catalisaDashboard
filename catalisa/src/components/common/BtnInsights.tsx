export default function BtnInsights ({isActive , isClick}:any){
    return (
        <button onClick={isClick} className={`w-45 h-full ${isActive ? "bg-[#2B1CD3] text-white" : "bg-gray-200 text-black" } rounded-r-lg cursor-pointer`}>Insights</button>
    )
}